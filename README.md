# OSM Query Handler
OSM Query Handler is intended to search geographical objects into OpenStreetMap (OSM).  
Search is handling by pushing the GET query to Nominatim - instrument for searching in OSM.  
From Nominatim comes JSON string that parse by Jackson to geographical object. 
Objects have a lot of info and main of it is a GeoJSON.  
Objects can return coordinate array of their biggest area. 
Also they can return coordinate of their center.

##### GeoJSON
GeoJSON spec define a lot of object types. But in this program used only four of them:
+ MultiPolygon
+ Polygon
+ LineString
+ Point

There is only four types because most of searching queries to OSM return one of this types.

##### Calculating
All calculates neglect spheroidality of Earth.  
While calculating the coordinate array of the objects with different types of GeoJSON - 
different arrays can be return:
+ Point - array with only one coordinate
+ LineString - array with all LineString coordinates
+ Polygon - array with only polygon surface coordinates
+ MultiPolygon - array with coordinates of polygon with biggest area

##### Caching
Queries and chained objects put in cache. Cache is represented by LinkedHashMap.   
When the cache is full, the last requested item is removed from the cache. 
So it use Least Recently Used (LRU) algorithm.  
Size of the cache can be set by .properties file
 with property **Cache_size**. If there is no .properties file or there is no property 
 **Cache_size** in it then cache size will set to 100 elements.   

#### Technologies
+ JDK 12
+ Jackson
+ GeoJSON

#### See also
+ [OpenStreetMap](https://www.openstreetmap.org/)
+ [GeoJSON spec](https://tools.ietf.org/html/rfc7946)
+ [Nominatim spec](https://nominatim.org/release-docs/latest/)