IsaaClock
=========

IsaaClock is an alarm clock with gamification mechanisms, that uses IsaaCloud API to create and manage games, achievements and user leaderboards.

IsaaClock requires:
<ul>
<li> Eclipse Kepler (4.3.2) with Android Developer Tools plugin installed.
<li> Android SDK with minimum version of 2.2 (API level 8).
<li> Android Support library v4.
<li> ActionBarSherlock support library, available for download at http://actionbarsherlock.com/download.html
<li> IsaaCloud Android SDK, found at https://github.com/isaacloud/android-sdk
</ul>

Detailed Installation
=====================

<ol> 
<li> Download Android SDK at: https://developer.android.com/sdk . You can download an entire Eclipse + Android SDK bundle or download the 
stand-alone version of the SDK. This guide covers the latter. If you choose to download the bundle, you can skip steps 2-4.
<li> Download Eclipse IDE from the developers website: https://www.eclipse.org/downloads .
<li> Go to Help / Eclipse Marketplace and search for "Android Developer Tools for Eclipse" and download the plugin.
<li> Make sure you have the right perspective open. Go to Window/Open perspective/Other  and choose "Java".
<li> Launch Android SDK Manager from the Eclipse toolbar and download "Tools", "Extras" and the latest version of the Android API.
<li> Download ActionBarCherlock support library from  http://actionbarsherlock.com/download.html and import the project to your Eclipse workspace.
<li> Download IsaaCloud Android SDK from https://github.com/isaacloud/android-sdk and import the project to your Eclipse workspace.
<li> Download the IsaaClock project and import the project to your Eclipse workspace.
<li> Make sure the IsaaClock build paths are correctly set. Right-click the project in your workspace and choose Properties/Java Build Path. 
Set IsaaClock/src and IsaaClock/gen as build paths.
</ol>

Account setup
=============
The IsaaClock application relies on gamification mechanisms created on individual IsaaCloud accounts. Two things are required for establishing 
connection between the application and the account: the gamification ID (which is the individual ID number of your gamification) and the application secret 
(which is the unique ID of the application created in your account). Both of these values need to be stored in the Settings.java class located in the 
src/pl/sointeractive/isaaclock/config folder.

By default, these values are set to an example gamification and application. This example account has implemented simple games and achievements 
that reward the user for setting alarms and waking up. Feel free to experiment with IsaaClock using the default id's before creating your own account 
and implementing your own games. 
 
If you wish to create your own games, it is necessary to setup an IsaaCloud account. Visit https://isaacloud.com to log into your account or 
sign up for a new one.

After logging into your account:
<ol>
<li> Create a new Gamification no the Profile page. Note down the ID of the created gamification.
<li> Go to the created gamification page and on the "Dashboard" tab and create a new Application. Same as before, note down the Secret of the created Application.
<li> In your Settings.java class set the gamificationId and appSecret values using the id/secret noted from your account.
</ol>

In the "API Console" tab you can create new users, games, achievements, leaderboards, send events, check notifications and much more. 
For detailed information on how to create new games please visit our site https://isaacloud.com/getting-started .

Launching IsaaClock
===================
In order to run the application on an Android device or emulator, right-click the project name in your workspace and choose Run As/Android Application. 
Next, choose your target device.




