package an.maguste.android.navier.services

class BackgroundUpdate {
// TODO
//    Plan:
//    Step 1. Connect Workmanager to project
//    Step 2. Set interval as 8 hour
//    Step 3. Add constraint device must be connected on wifi and device must be connected to power
//    Step 4. Get movies and update database
//    Step 5*. Have one edge case that if we launch the app, load data from the cache,
//    and after that our workManager starts and updated cache.
//    We don’t get fresh data on the UI. To fix this you need to add a trigger to SQLite like let me know if the table changed.
//    Sounds like something hard but via room you can easily do it.

    // наследуемся Worker - override doWork
    // WorkRequest + // Constraint on internet and charging

    // WorkManager

}