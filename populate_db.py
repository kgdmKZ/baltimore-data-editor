import pymysql.cursors
import pymysql
import requests
import sys
import os

neighborhoods_areas = dict() 

"""strip_words = set(["New", "North", "South", "East", "West", "St", "St.", "Saint",
    "Hill", "Hills", "Village", "Lake", "Station", "The", "Old", "Park", "Square",
    "Gardens", "Mount", "Mt", "Mt.", "Market", "Town", "Heights", "Point", "Port",
    "Land", "Greater", "Outer", "Inner", "Upper", "Lower", "Ridge", "Mid", "Middle",
    "Street", "And", "&"])"""

def stringIsFloat(st):
    try:
        float(st)
        return True
    except:
        return False

def recase(place):
    precede_upper = ['/', '-', ' ']
    chars = list(place)
    for i in xrange(len(chars)-1):
        mc_case = i > 0 and chars[i-1] == 'M' and chars[i] == 'c'
        if (chars[i] not in precede_upper) and not mc_case:
            chars[i+1] = chars[i+1].lower()
    return ''.join(chars)

def getArea(place, areas):
    place = recase(place)
    if place in neighborhoods_areas:
        return areas[neighborhoods_areas[place]]
    if place in areas:
        return areas[place]
    print("Unmatched place %s\n" % place)
    return None

def convertFmt(col, val):
    if col == 'date':
        val = 'STR_TO_DATE("%s", "%m/%d/%Y")' % (val.split('T')[0])
    elif col == 'location':
        val = 'POINT(%f,%f)' % tuple(val['coordinates'])
    elif (not stringIsFloat(val)) and (val != 'NULL'):
        val = '"' + val + '"'
    return val

def insertIntoTable(data, table_name, areas, cur):
    cols = [col for col in data[0] if col[0] != ':']
    
    if table_name != 'area':
        cols += ['area_id']
        for i in xrange(len(data)):
            area = getArea(data[i]['neighborhood'], areas)
            data[i]['area_id'] = str(area)

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

def createDemoTable(row, cur):
    demo_table = '''CREATE TABLE IF NOT EXISTS area (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      csa2010 VARCHAR(255) NOT NULL UNIQUE, 
      male10 INT unsigned NOT NULL,
      tpop10 INT unsigned NOT NULL,
      female10 INT unsigned NOT NULL,
      '''
    
    skip = ['csa2010', 'male10', 'tpop10', 'female10']
    
    for key in row:
        if key in skip:
            continue
        demo_table += key + " DECIMAL(6,3) NOT NULL,"
    
    demo_table += 'PRIMARY KEY (id))'
    cur.execute(demo_table)

def finish(db, cur, data_type, status_code):
    print("Failed to get %s data with status code %d\n" % (data_type, status_code))
    cur.close()
    db.close()

def generateNeighborhoodAreas():
    with open('csa_neighborhoods', 'r') as file:
        csa_neighborhoods = file.read().splitlines()
    for i in xrange(len(csa_neighborhoods)/3+1):
        csa = csa_neighborhoods[3*i]
        neighborhoods = csa_neighborhoods[3*i+1].split(',')
        for neighborhood in neighborhoods:
            neighborhoods_areas[neighborhood] = csa
    
def getData():
    if not (len(sys.argv) == 3):
        print("You must provide a user and password for MySQL")
        return
    generateNeighborhoodAreas()
    db = pymysql.connect(host='localhost', user=sys.argv[1], password=sys.argv[2])
    cur = db.cursor()
    cur.execute('CREATE DATABASE IF NOT EXISTS baltimore_neighborhoods')
    cur.execute('USE baltimore_neighborhoods')

    demo_resp = requests.get('https://data.baltimorecity.gov/resource/tgip-gtj8.json')
    if demo_resp.status_code != 200:
        finish(db, cur, 'demographic', demo_resp.status_code)
        return

    demographics = demo_resp.json()
    createDemoTable(demographics[0], cur) 

    grocery_resp = requests.get('https://data.baltimorecity.gov/resource/8gms-s9we.json')
    
    if grocery_resp.status_code != 200:
        finish(db, cur, 'grocery store', grocery_resp.status_code)
        return
    grocery_stores = grocery_resp.json()
    
    grocery_sql = '''CREATE TABLE IF NOT EXISTS grocery_store (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      type VARCHAR(255) NOT NULL,
      zipcode INT unsigned NOT NULL,
      neighborhood VARCHAR(255) NOT NULL,
      councildistrict INT unsigned NOT NULL,
      policedistrict VARCHAR(255) NOT NULL,
      location_1_address VARCHAR(255) NOT NULL,
      location_1_state VARCHAR(255) NOT NULL,
      location_1_city VARCHAR(255) NOT NULL,
      area_id INT unsigned NOT NULL,
      FOREIGN KEY area_demographics(area_id)
      REFERENCES area(id)
      ON UPDATE CASCADE,
      PRIMARY KEY (id)
    )'''
    cur.execute(grocery_sql)
    
    
    lib_resp = requests.get('https://data.baltimorecity.gov/resource/5z6v-z7uh.json')
    if lib_resp.status_code != 200:
        finish(db, cur, 'library', lib_resp.status_code)
        return
    libraries = lib_resp.json()
    library_sql = '''CREATE TABLE IF NOT EXISTS library (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      neighborhood VARCHAR(255) NOT NULL,
      councildistrict INT unsigned NOT NULL,
      zipcode INT unsigned NOT NULL,
      policedistrict VARCHAR(255) NOT NULL,
      location_1_address VARCHAR(255) NOT NULL,
      location_1_state VARCHAR(255) NOT NULL,
      location_1_city VARCHAR(255) NOT NULL,
      area_id INT unsigned NOT NULL,
      FOREIGN KEY area_demographics(area_id)
      REFERENCES area(id)
      ON UPDATE CASCADE,
      PRIMARY KEY (id)
    )'''
    cur.execute(library_sql)
    
    vacant_resp = requests.get('https://data.baltimorecity.gov/resource/rw5h-nvv4.json?$limit=16648')
    if vacant_resp.status_code != 200:
        finish(db, cur, 'vacant building', vacant_resp.status_code)
        return
    vacant_bldgs = vacant_resp.json()    
    vacant_sql = '''CREATE TABLE IF NOT EXISTS vacant_building (
      id INT unsigned NOT NULL AUTO_INCREMENT,
      referenceid VARCHAR(255) NOT NULL,
      block VARCHAR(255) NOT NULL,
      lot VARCHAR(255) NOT NULL,
      buildingaddress VARCHAR(255) NOT NULL,
      noticedate DATE NOT NULL,
      neighborhood VARCHAR(255) NOT NULL,
      policedistrict VARCHAR(255) NOT NULL,
      councildistrict INT NOT NULL,
      location POINT NOT NULL,
      area_id INT unsigned NOT NULL,
      FOREIGN KEY area_demographics(area_id)
      REFERENCES area(id)
      ON UPDATE CASCADE,
      PRIMARY KEY (id)
    )'''
    cur.execute(vacant_sql)
    
    #now actually insert rows into database tables
    areas = {demographics[i]['csa2010'] : i+1 for i in xrange(len(demographics))}
    insertIntoTable(demographics, 'area', areas, cur)
    insertIntoTable(grocery_stores, 'grocery_store', areas, cur)
    insertIntoTable(libraries, 'library', areas, cur)
    insertIntoTable(vacant_bldgs, 'vacant_building', areas, cur)

    cur.close()
    db.commit()
    db.close()

if __name__ == '__main__':
    getData()
    