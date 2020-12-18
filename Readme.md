# Chip Assignment.

Tech stack:
-Uses 100% kotlin.

Used Libraries:
-Hilt, for DI
-Architecture components.
-Glide, for image loading.
-Coroutines - for multi threading tasks, such as Network requests.
-Lottie for animations. (loading)

Architecture:
I have used MVVM architecture, sticking to Android recommendations.

Network Requests:
I believe the relatively static info, should not be queried, every time the view is presented, therefor for the breeds list, I query once a day,
however, the images will be queried, every time, since we want different images every time we select a breed.

Design:
For some reason, I believe, I coding skills are better then designing. However,
I try to aim for a good balance, of animations, and clean and clear appearance of the app. Hope its good enough.

Testing:
-Have implemented a small amount of tests, just a taste of UI testing (test unit tests, with Robolectric framework)






