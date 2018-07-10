# Lite Github

## Introduction
&#160; &#160; &#160; &#160;There are quite a few patterns out there for app architectures, such as MVP, MVVM, and unidirectional. I’m a huge fan of unidirectional, and I like MVVM too. There are 3 parts of M-V-VM, the Model, View, and ViewModel. Let’s take a look at what each of these are.
- View — This is the UI component that is in a layout and rendered on the display.
- ViewModel — Views subscribe to the data they are interested in from the ViewModel. So when this data changes it can be broadcast to any interested observers. 
	- The ViewModel is responsible for preparing data for consumption by the View.
	- The ViewModel’s state is stable across the lifecycle of Activities and Fragments. So as an Activity is torn down and rebuilt (on an orientation change) it can use the same ViewModel. And you can scope the lifetime of these ViewModels to the Activity lifetime as well, so that when the Activity is finished (not destroyed), then the ViewModel can be cleaned up.
	- ViewModels should not hold references to any Views. And if they need an ApplicationContext, then you can use AndroidViewModel which supplies this.
- LiveData —An interesting piece of a ViewModel that you can wrap any data that changes quite rapidly and these changes have to be reflected in UI components.
- Model — This is where your underlying data is actually stored. The model can be backed by local persistence stores, and synchronized with remote data sources. You can use Room to make it easier to work with SQLLite (on device) and have Room generate the ORM layer for you. Or you can use Firebase for persistence, which automagically syncs its state across multiple platforms. You have a lot of flexibility in what you choose. You can even use something like Redux in addition to Firebase to be your Model.

The following diagram shows all the modules in our recommended architecture and how they interact with one another
![image](https://github.com/journeyOS/LiteWeather/blob/master/resource/final-architecture.png)

## Lite Github(github client)
&#160; &#160; &#160; &#160;A Android project(github client) demonstrating the use of Retrofit and MVVM to interact with web services.

[Download App](https://github.com/journeyOS/LiteGithub/blob/master/resource/download_barcode.png)


![image](https://github.com/journeyOS/LiteGithub/blob/master/resource/starred.png)
![image](https://github.com/journeyOS/LiteGithub/blob/master/resource/my_repos.png)
![image](https://github.com/journeyOS/LiteGithub/blob/master/resource/search.png)
![image](https://github.com/journeyOS/LiteGithub/blob/master/resource/issues.png)
![image](https://github.com/journeyOS/LiteGithub/blob/master/resource/settings.png)

## Docs
[Guide](https://developer.android.com/topic/libraries/architecture/guide)
[Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
[Livedata](https://developer.android.com/topic/libraries/architecture/livedata)
[Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel)
[Room](https://developer.android.com/topic/libraries/architecture/room)
[Build app with arch component](https://codelabs.developers.google.com/codelabs/build-app-with-arch-components/index.html?index=..%2F..%2Findex#0)
[Video](https://caster.io/courses/android-architecture-components-deep-dive)

## Open source code
[Butterknife](https://github.com/JakeWharton/butterknife)
[Retrofit](https://github.com/square/retrofit)
[Leakcanary](https://github.com/square/leakcanary)
[Calligraphy](https://github.com/chrisjenx/Calligraphy)
[Stetho](https://github.com/facebook/stetho)
......

## License
&#160; &#160; &#160; &#160;This application is Free Software: You can use, study share and improve it at your will. Specifically you can redistribute and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, without distribute, sublicense, and/or sell copies of the Software.