Randomizer

<p align="center"><a href='https://ko-fi.com/Z8Z4XWSM' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi4.png?v=2' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a></p>

Initialize variables with Random values

Download
--------

Download the latest JAR or grab via Maven:
```xml
<dependency>
  <groupId>com.kostasdrakonakis</groupId>
  <artifactId>randomizer</artifactId>
  <version>1.0.1</version>
</dependency>
```
```xml
<dependency>
  <groupId>com.kostasdrakonakis</groupId>
  <artifactId>randomizer-compiler</artifactId>
  <version>1.0.1</version>
</dependency>
```

or via Gradle: 

Java:
```groovy
implementation 'com.kostasdrakonakis:randomizer:1.0.1'
annotationProcessor 'com.kostasdrakonakis:randomizer-compiler:1.0.1'
```

Kotlin:
```groovy
implementation 'com.kostasdrakonakis:randomizer:1.0.1'
kapt 'com.kostasdrakonakis:randomizer-compiler:1.0.1'
```
Usage
-----

You can use it in Activities or Fragments like:

Java

```java
public class MainActivity extends AppCompatActivity {
    
    @RandomInt
    int withoutRange;
    
    @RandomInt(minValue = 10, maxValue = 100)
    int withRange;
    
    @RandomString
    String normalString;
    
    @RandomString(uuid = true)
    String uuidString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Randomizer.bind(this);
    }
}
```

Kotlin

```kotlin
class MainActivity : AppCompatActivity() {

    @RandomInt
    @JvmField var withoutRange: Int = 0
    
    @RandomInt(minValue = 10, maxValue = 100)
    @JvmField var withRange: Int = 0
    
    @RandomString
    lateinit var normalString: String
    
    @RandomString(uuid = true)
    lateinit var uuidString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Randomizer.bind(this)
        setContentView(R.layout.activity_main)
    }
}
```

Supported Annotations:
```text
@RandomInt // optional values: minValue and maxValue
@RandomString // optional value: uuid
@RandomFloat // optional values: minValue and maxValue
@RandomDouble // optional values: minValue and maxValue
@RandomChar 
@RandomLong // optional values: minValue and maxValue
@RandomShort
```

CHANGELOG
----
**v1.0.0**:
* Add support for int, String, float, char, double, long, short types
* Add support for initializing with custom range

TODO
----
+ Add support for objects

Feel free to submit PR's. Also open to suggestions!

License
-------

 Copyright 2018 Kostas Drakonakis

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
