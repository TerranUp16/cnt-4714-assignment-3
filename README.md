# Assignment 3
![Screenshot](https://github.com/TerranUp16/cnt-4714-assignment-3/blob/main/screenshots/start.png)
This project was created under the direction of Dr. Mark Llewellyn for University of Central Florida course CNT-4714.

This project calls for a Java implementation of a GUI MySQL client. The project only requires access to a `project3` databse with predefined DDL.

# Dependencies
* [Java 15](https://www.oracle.com/java/technologies/javase-downloads.html)
* [Apache Maven](https://maven.apache.org/download.cgi)

# Install and Run
## Using `git`
```
git clone https://github.com/TerranUp16/cnt-4714-assignment-3.git ;
cd cnt-4714-assignment-3 ;
mvn javafx:run ;
```

## Download Code
1. [Download `.zip`](https://github.com/TerranUp16/cnt-4714-assignment-3/archive/master.zip)
2. Unzip `cnt-4714-assignment-3-master.zip`
3. Open a shell/terminal application (ex- PowerShell on Windows, Terminal on Mac)
4. Navigate to extracted `cnt-4714-assignment-3-master` directory/folder
5. Run `mvn javafx:run`

## Troubleshooting
> `mvn` not found

Please review [Maven's installation and configuration documentation](https://maven.apache.org/install.html). In particular, make sure `mvn` is configured for `PATH` for the shell/terminal you are using.

> `JAVA_HOME` not found

Please review [Maven's installation and configuration documentation](https://maven.apache.org/install.html). In particular, make sure `JAVA_HOME` is configured for `PATH` for the shell/terminal you are using.

Please also review Oracle's documentation on this for-

* [Windows](https://docs.oracle.com/en/java/javase/14/install/installation-jdk-microsoft-windows-platforms.html#GUID-96EB3876-8C7A-4A25-9F3A-A2983FEC016A)
* [Mac](https://docs.oracle.com/en/java/javase/14/install/installation-jdk-macos.html#GUID-F9183C70-2E96-40F4-9104-F3814A5A331F)
* [Linux](https://docs.oracle.com/en/java/javase/14/install/installation-jdk-linux-platforms.html#GUID-737A84E4-2EFF-4D38-8E60-3E29D1B884B8)

# Connect to a MySQL Database
Currently, the application only supports looking for a `project3` database available at a MySQL instance running on `localhost:3306`. Accordingly, you will need to have a MySQL server running on `localhost:3306` with a `project3` database created in it.

You may use fully custom MySQL users and passwords though to authenticate. The client uses `mysql_native_password` for this.

# Use Included `docker-compose.yml` to Deploy a MySQL Server
If you have [Docker Desktop](https://docs.docker.com/desktop/) (or an equivalent Docker setup), you can open a shell/terminal in the main directory of this repository and run-

```
docker-compose up
```

This will activate `docker-compose.yml` which creates a MySQL 8 Docker container which exposes port `3306` to `localhost`.

If you need the `root` password to access this MySQL instance, you can find it in `docker-compose.yml`.

## Use `prepareDB.ps1` to Load Sample User and Data
As long as the MySQL 8 container from the included `docker-compose.yml` is up and running, you can run-

```
powershell.exe prepareDB.ps1
```

This script will connect to the container, create a `project3` database, load four tables (and their data) into `project3`, create a `client@%` MySQL user (whose password is `client`), and grant `client@%` `SELECT` access to all tables in the `project3` database.

## Sample Screenshot
![Query performed on sample data](https://github.com/TerranUp16/cnt-4714-assignment-3/blob/main/screenshots/client_3c.png)
