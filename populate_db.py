import pymysql.cursors
import pymysql
import requests
import os

(neighborhoods_areas, areas, cur, address_ids, neighborhood_ids, 
    location_ids, police_ids, zipcode_ids) = (None,)*8

"""strip_words = set(["New", "North", "South", "East", "West", "St", "St.", "Saint",
    "Hill", "Hills", "Village", "Lake", "Station", "The", "Old", "Park", "Square",
    "Gardens", "Mount", "Mt", "Mt.", "Market", "Town", "Heights", "Point", "Port",
    "Land", "Greater", "Outer", "Inner", "Upper", "Lower", "Ridge", "Mid", "Middle",
    "Street", "And", "&"])"""

non_capitalized = set(['a', 'an', 'the', 'at', 'for', 'in', 'of', 'on', 'to', 
    'up', 'and'])

def stringIsFloat(st):
    try:
        float(st)
        return True
    except:
        return False

def recase(place):
    place = place.strip()

    if place.split()[-1] == 'st':
        place = place[:-2] + 'St'
    
    precede_upper = ['/', '-', ' ']
    chars = list(place)
    
    for i in xrange(len(chars)-1):
        mc_case = i > 0 and chars[i-1] == 'M' and chars[i] == 'c'
        mid_word = (chars[i] not in precede_upper) and not mc_case
        if mid_word:
            chars[i+1] = chars[i+1].lower()
    
    recased = ''.join(chars).replace(' - ', '-')
    for word in recased.split():
        if word.lower() in non_capitalized:
            recased = recased.replace(' ' + word + ' ', 
              ' ' + word.lower() + ' ')
    
    return recased

def convertFmt(col, val):
    if col == 'street_address' and val.startswith('NOT PROVIDED'):
        val = 'NULL'
    elif col == 'noticedate':
        val = 'STR_TO_DATE("%s", "%%Y-%%m-%%d")' % (val.split('T')[0])
    elif col == 'coords':
        val = 'POINT(%f,%f)' % (val[1], val[0])
    elif (not stringIsFloat(val)) and (val != 'NULL'):
        val = '"' + recase(val) + '"'
    else:
        val = str(val)
    return val

def insertIntoTable(data, table_name):
    cols = [col for col in data[0] if (col[0] != ':')]

    insert_sql = 'INSERT INTO ' + table_name + ' (' 
    
    for col in cols:
        insert_sql += col + ','
    insert_sql = insert_sql[:-1] + ') VALUES '
    
    for row in data:
        value_sql = '('    
        
        for col in cols:
            val = convertFmt(col, row[col])
            value_sql += val + ','
        
        insert_sql += value_sql[:-1] + ")," 
    
    cur.execute(insert_sql[:-1])

def createAreaTable(demographics):
    demo_table = '''CREATE TABLE IF NOT EXISTS area (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      csa2010 VARCHAR(255) UNIQUE NOT NULL, 
      male10 INT unsigned NOT NULL,
      female10 INT unsigned NOT NULL,
      hhsize10 DECIMAL(2,1) unsigned NOT NULL,
      racdiv10 DECIMAL(4,1) unsigned NOT NULL,
      '''
    
    skip = ['csa2010', 'male10', 'tpop10', 'female10', 'hhsize10', 
      'racdiv10']
    
    for key in demographics[0]:
        if key not in skip:
            demo_table += key + " DECIMAL(4,3) unsigned NOT NULL,"
    
    demo_table += '''
      PRIMARY KEY(id))'''
    cur.execute(demo_table)
    
    for area in demographics:
        del area['tpop10']
    
    insertIntoTable(demographics, 'area')

def finish(db, data_type, status_code):
    print("Failed to get %s data with status code %d\n" % (data_type, status_code))
    cur.close()
    db.close()

def generateNeighborhoodAreas():
    global neighborhoods_areas
    
    with open('csa_neighborhoods', 'r') as file:
        csa_neighborhoods = file.read().splitlines()
    
    neighborhoods_areas = dict()
    
    for i in xrange(len(csa_neighborhoods)/3+1):
        csa = csa_neighborhoods[3*i]
        neighborhoods = csa_neighborhoods[3*i+1].split(',')
        for neighborhood in neighborhoods:
            neighborhoods_areas[neighborhood] = csa

def createNeighborhoodTable():
    global neighborhood_ids
    generateNeighborhoodAreas()
    neighborhood_table = '''CREATE TABLE IF NOT EXISTS neighborhood (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) UNIQUE NOT NULL,
      area_id INT unsigned NOT NULL,
      FOREIGN KEY(area_id)
      REFERENCES area(id)
      ON UPDATE CASCADE,
      PRIMARY KEY(id)
    )'''
    cur.execute(neighborhood_table)
    
    add_neighborhoods = []
    counter = 0
    neighborhood_ids = {}
    
    for neighborhood in neighborhoods_areas.keys():
        neighborhood_data = {
          'name' : neighborhood,
          'area_id' : areas[neighborhoods_areas[neighborhood]]
        }
        add_neighborhoods += [neighborhood_data]
        counter += 1
        neighborhood_ids[neighborhood] = counter

    insertIntoTable(add_neighborhoods, 'neighborhood')

def getNeighborhoodId(neighborhood_name):
    if neighborhood_name == 'CARE' or neighborhood_name == 'Care':
        return neighborhood_ids['CARE']
    
    if neighborhood_name not in neighborhood_ids:
        neighborhood_name = recase(neighborhood_name)
    
    return neighborhood_ids[neighborhood_name]

def extractAddressInfo(data_dict):
    global not_provided_count
    councildistrict = data_dict['councildistrict']
    district_name = recase(data_dict['policedistrict'])
    
    if district_name == 'Notheastern':
        district_name = 'Northeastern'
    
    policedistrict = police_ids[district_name]
    neighborhood_id = getNeighborhoodId(data_dict['neighborhood'])
    
    if 'location_1_address' in data_dict:
        street_address = recase(data_dict['location_1_address'])
    else:
        street_address = recase(data_dict['buildingaddress'])
        if street_address == '0':
            not_provided_count += 1
            street_address = 'NOT PROVIDED %d' % not_provided_count
    
    return (councildistrict, policedistrict, neighborhood_id, street_address)

def revertToDict(tuple_vals, is_address=True):
    if is_address:
        keys = ['councildistrict', 'policedistrict', 'neighborhood_id', 'street_address']
    else:
        keys = ['coords', 'block_number', 'lot_number', 'address_id']
    return {keys[i] : tuple_vals[i] for i in xrange(len(tuple_vals))}

def extractLocationInfo(data_dict):
    global not_provided_count
    
    coords = tuple(data_dict['location']['coordinates'])
    block = data_dict['block']
    lot = data_dict['lot']
    neighborhood_id = getNeighborhoodId(data_dict['neighborhood'])
    address = recase(data_dict['buildingaddress'])
    
    if address == '0':
        not_provided_count += 1
        address = 'NOT PROVIDED %d' % not_provided_count
    
    addr_key = (address, neighborhood_id)
    address_id  = address_ids[addr_key]
    return (coords, block, lot, address_id)

def makeAddressIds(addresses):
    global address_ids

    street_addrs = [(a['street_address'], a['neighborhood_id']) for a in addresses]
    address_ids = {street_addrs[i] : i+1 for i in xrange(len(street_addrs))}

def createAddressTable(libraries, grocery_stores, vacant_buildings):
    global not_provided_count
    not_provided_count = 0
    
    address_table = '''CREATE TABLE IF NOT EXISTS address (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      street_address VARCHAR(255),
      councildistrict INT unsigned NOT NULL,
      policedistrict INT unsigned NOT NULL,
      neighborhood_id INT unsigned NOT NULL,
      FOREIGN KEY(neighborhood_id)
      REFERENCES neighborhood(id)
      ON UPDATE CASCADE,
      FOREIGN KEY(councildistrict)
      REFERENCES council_district(id)
      ON UPDATE CASCADE,
      FOREIGN KEY(policedistrict)
      REFERENCES police_district(id)
      ON UPDATE CASCADE,
      UNIQUE(neighborhood_id, street_address),
      PRIMARY KEY(id)
    )'''
    cur.execute(address_table)
    
    addresses = set([extractAddressInfo(library) for library in libraries])
    addresses.update([extractAddressInfo(store) for store in grocery_stores])
    addresses.update([extractAddressInfo(bldg) for bldg in vacant_buildings])
    
    addresses = [revertToDict(a) for a in addresses]
    
    makeAddressIds(addresses)
    insertIntoTable(addresses, 'address')
    
    not_provided_count = 0

def createLocationTable(vacant_buildings):
    global location_ids
    
    location_table = '''CREATE TABLE IF NOT EXISTS location (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      coords POINT UNIQUE NOT NULL,
      block_number VARCHAR(255) NOT NULL,
      lot_number VARCHAR(255) NOT NULL,
      address_id INT UNSIGNED UNIQUE NOT NULL,
      FOREIGN KEY(address_id)
      REFERENCES address(id)
      ON UPDATE CASCADE,
      UNIQUE(block_number, lot_number),
      PRIMARY KEY(id)
    )'''
    cur.execute(location_table)
    
    locations = list(set([extractLocationInfo(bldg) for bldg in vacant_buildings]))
    location_ids = {locations[i][0] : i+1 for i in xrange(len(locations))}
    
    locations = [revertToDict(location, False) for location in locations]
    
    insertIntoTable(locations, 'location')

def extractPOIInfo(data_dict):
    neighborhood_id = getNeighborhoodId(data_dict['neighborhood'])
    address = recase(data_dict['location_1_address'])
    addr_key = (address, neighborhood_id)
    
    poi = {'name' : data_dict['name']}
    poi['address_id'] = address_ids[addr_key]
    poi['zipcode'] = zipcode_ids[int(data_dict['zipcode'])]
    
    if 'type' in data_dict:
        poi['type'] = data_dict['type']
    
    return poi

def createGroceryTable(grocery_stores):
    grocery_sql = '''CREATE TABLE IF NOT EXISTS grocery_store (
      id INT UNSIGNED NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      type INT UNSIGNED NOT NULL,
      zipcode INT UNSIGNED NOT NULL,
      address_id INT UNSIGNED UNIQUE NOT NULL,
      FOREIGN KEY(address_id)
      REFERENCES address(id)
      ON UPDATE CASCADE,
      FOREIGN KEY(zipcode)
      REFERENCES zip_code(id)
      ON UPDATE CASCADE,
      FOREIGN KEY(type)
      REFERENCES grocery_type(id)
      ON UPDATE CASCADE,
      PRIMARY KEY(id)
    )'''
    cur.execute(grocery_sql)
    
    stores = [extractPOIInfo(store) for store in grocery_stores]
    for store in stores:
        if store['type'] == 'Limited Supermarket':
            store['type'] = 1
        elif store['type'] == 'Small Supermarket':
            store['type'] = 2
        else:
            store['type'] = 3
    
    insertIntoTable(stores, 'grocery_store')

def createLibraryTable(libraries):
    library_sql = '''CREATE TABLE IF NOT EXISTS library (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      zipcode INT unsigned NOT NULL,
      address_id INT unsigned UNIQUE NOT NULL,
      FOREIGN KEY(address_id)
      REFERENCES address(id)
      ON UPDATE CASCADE,
      FOREIGN KEY(zipcode)
      REFERENCES zip_code(id)
      ON UPDATE CASCADE,
      PRIMARY KEY (id)
    )'''
    cur.execute(library_sql)
    
    libs = [extractPOIInfo(library) for library in libraries]
    insertIntoTable(libs, 'library')

def extractVacantInfo(data_dict):
    bldg = {'referenceid':data_dict['referenceid'], 
      'noticedate':data_dict['noticedate']}
    loc_key = tuple(data_dict['location']['coordinates'])
    bldg['location_id'] = location_ids[loc_key]
    return bldg

def createVacantTable(vacant_buildings):
    vacant_sql = '''CREATE TABLE IF NOT EXISTS vacant_building (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      referenceid VARCHAR(255) UNIQUE NOT NULL,
      noticedate DATE NOT NULL,
      location_id INT unsigned NOT NULL,
      FOREIGN KEY(location_id)
      REFERENCES location(id)
      ON UPDATE CASCADE,
      PRIMARY KEY (id)
    )'''
    cur.execute(vacant_sql)

    vacant = [extractVacantInfo(bldg) for bldg in vacant_buildings]
    insertIntoTable(vacant, 'vacant_building')

def createCouncilDistricts():
    council_table = '''CREATE TABLE council_district (
      id INT UNSIGNED NOT NULL AUTO_INCREMENT,
      district_number INT UNSIGNED UNIQUE NOT NULL,
      PRIMARY KEY(id)
      );'''
    cur.execute(council_table)
    
    council_sql  = '''INSERT INTO council_district(district_number) 
      VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14);'''
    cur.execute(council_sql)

def createPoliceDistricts():
    global police_ids
    police_table = '''CREATE TABLE police_district (
      id INT UNSIGNED NOT NULL AUTO_INCREMENT,
      district_name VARCHAR(255) UNIQUE NOT NULL,
      PRIMARY KEY(id)
      );'''
    cur.execute(police_table)

    districts = ['Central', 'Southeastern', 'Eastern', 'Northeastern', 'Northern', 
    'Northwestern', 'Western', 'Southwestern', 'Southern']
    districts_str = '"),("'.join(districts)
    
    police_sql = 'INSERT INTO police_district(district_name) VALUES ("%s");' % districts_str
    cur.execute(police_sql)
    
    police_ids = {districts[i]: i+1 for i in xrange(len(districts))}

def createTypeTable():
    type_table = '''CREATE TABLE grocery_type (
      id INT UNSIGNED NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) UNIQUE NOT NULL,
      PRIMARY KEY(id)
    )'''
    cur.execute(type_table)

    types = '''INSERT INTO grocery_type(name) VALUES 
      ("Limited Supermarket"), ("Small Supermarket"), ("Full Supermarket");'''
    cur.execute(types)

def createZipTable():
    global zipcode_ids
    zip_table = '''CREATE TABLE zip_code (
      id INT UNSIGNED NOT NULL AUTO_INCREMENT,
      zip INT UNSIGNED UNIQUE NOT NULL,
      PRIMARY KEY(id)
      )'''
    cur.execute(zip_table)
    
    zipcodes = [21201,21202,21205,21206,21207,21208,21209,21210,21211,21212,
      21213,21214,21215,21216,21217,21218,21222,21223,21224,21225,21226,21227,
      21228,21229,21230,21231,21234,21236,21237,21239,21251]
    
    zipcodes_sql = '),('.join([str(zipcode) for zipcode in zipcodes])
    insert_zipcodes = 'INSERT INTO zip_code(zip) VALUES (%s);' % zipcodes_sql
    cur.execute(insert_zipcodes)

    zipcode_ids = {zipcodes[i] : i+1 for i in xrange(len(zipcodes))}

    
def getData():
    global areas, cur
    db = pymysql.connect(host='localhost', read_default_file='~/.my.cnf')
    cur = db.cursor()
    cur.execute('CREATE DATABASE IF NOT EXISTS baltimore_neighborhoods')
    cur.execute('USE baltimore_neighborhoods')

    demo_resp = requests.get('https://data.baltimorecity.gov/resource/tgip-gtj8.json')
    if demo_resp.status_code != 200:
        finish(db, 'demographic', demo_resp.status_code)
        return

    demographics = demo_resp.json()
    areas = {demographics[i]['csa2010'] : i+1 for i in xrange(len(demographics))}
    
    grocery_resp = requests.get('https://data.baltimorecity.gov/resource/8gms-s9we.json')
    
    if grocery_resp.status_code != 200:
        finish(db, 'grocery store', grocery_resp.status_code)
        return
    grocery_stores = grocery_resp.json()
    
    lib_resp = requests.get('https://data.baltimorecity.gov/resource/5z6v-z7uh.json')
    if lib_resp.status_code != 200:
        finish(db, 'library', lib_resp.status_code)
        return
    libraries = lib_resp.json()
    
    vacant_resp = requests.get('https://data.baltimorecity.gov/resource/rw5h-nvv4.json?$limit=16648')
    if vacant_resp.status_code != 200:
        finish(db, 'vacant building', vacant_resp.status_code)
        return
    vacant_bldgs = vacant_resp.json()    
    
    createAreaTable(demographics) 
    createNeighborhoodTable()
    createCouncilDistricts()
    createPoliceDistricts()
    createAddressTable(libraries, grocery_stores, vacant_bldgs)
    createLocationTable(vacant_bldgs)
    createTypeTable()
    createZipTable()
    createGroceryTable(grocery_stores)
    createLibraryTable(libraries)
    createVacantTable(vacant_bldgs)
    
    cur.close()
    db.commit()
    db.close()

if __name__ == '__main__':
    getData()
    