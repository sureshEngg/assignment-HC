# DeliveryProject for Android Users
Project includes features like displaying pending Deliveries along with the gps location so that delivery can be
completed easily and hasslefree.

### Functionality
The app is composed of 2 screens.

#### Delivery List
This is a fragment to allow a delivery boy to view all pending Deliveries.
Each page result is kept in the database in `delivery` table.

While doing load more each time a new page is fetched, the same result record in the
Database is updated with the new list of deliveries.

**NOTE** The UI loads initially only 20 deliveries and every load more attempt sets next 20 deliveries gets displayed on
the screen

#### DeliveryDetail
This is a fragment to allow the user to get the gps location along with the description of the delivery with the help of
 Google map

### Build and Run
1) Run Android Studio IDE
2) Click File -> Open
3) Select the project in browse to import the project
4) Wait for the gradle syncing
5) Click on Run or press F5

### Libraries
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Android Data Binding][data-binding]
* [Dagger 2][dagger2] for dependency injection
* [Retrofit][retrofit] for REST api communication
* [Glide][glide] for image loading
* [espresso][espresso] for UI tests
* [mockito][mockito] for mocking in tests
* [paging][paging] for paging in list and web api

[mockwebserver]: https://github.com/square/okhttp/tree/master/mockwebserver
[support-lib]: https://developer.android.com/topic/libraries/support-library/index.html
[arch]: https://developer.android.com/arch
[data-binding]: https://developer.android.com/topic/libraries/data-binding/index.html
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
[dagger2]: https://google.github.io/dagger
[retrofit]: http://square.github.io/retrofit
[glide]: https://github.com/bumptech/glide
[mockito]: http://site.mockito.org
[paging]: https://developer.android.com/topic/libraries/architecture/paging/

