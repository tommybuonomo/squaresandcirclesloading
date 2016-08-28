#Squares And Circles Loading Animation

A pretty loading animation inspired by [this post](https://dribbble.com/shots/2924374-Square-Circles) on [Dribble](https://dribbble.com)

![ezgif com-crop](https://cloud.githubusercontent.com/assets/15737675/18036093/2aa2463e-6d63-11e6-8c07-d95fd147fcef.gif) ![ezgif com-crop 1](https://cloud.githubusercontent.com/assets/15737675/18036129/d8324bfa-6d63-11e6-947c-9971ea62c1b6.gif)
![ezgif com-crop 2](https://cloud.githubusercontent.com/assets/15737675/18036200/d72c3f62-6d64-11e6-9f4e-103912eca3f6.gif)
##How to
####Gradle
```Gradle
dependencies {
    compile 'com.tbuonomo:squaresandcirclesloading:1.0.0'
}
```
####In your XML layout
```Xml
<com.tbuonomo.squaresandcirclesloading.SquaresAndCirclesLoadingView
      android:layout_width="100dp"
      android:layout_height="100dp"
      android:layout_centerInParent="true"
      app:backgroundColor="#0288D1"
      app:circlesColor="#FFF"
      app:interpolator="anticipate_overshoot"
      app:animationDuration="1400"/>
```
####Attributes
| Attribute | Description |
| --- | --- |
| `circlesColor` | Color of the circles |
| `squareColor` | Color of the square (must be the same of the parent background)  |
| `interpolator` | The interpolator to use for the points animation (by default `fastOutSlowIn`) |
| `animationDuration` | Step duration of the animation in ms (by default 800) |

##License
    Copyright 2016 Tommy Buonomo
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
