# SamaanPK - Android App

## Project Overview:

This is the clone of an e-commerce app made by Pakistani Youtuber Bilal Munir. Currently this project is in progress. Only the login, sign up and profile UI is designed
in this project.

## Setup: 

### Prerequisites:
* Android Studio: Latest version
* Kotlin: Ensure Kotlin support is enabled in Android Studio.
* Android SDK: Ensure the SDK is set up in Android Studio.

### Steps:
* Clone the Repository
* Open the Project in Android Studio
* Build the Project
* Run on Emulator/Device

## Screens Designed and Their Purpose
1. Login Screen:
Allows users to log into the application using their email and password.
Includes fields for entering credentials and a button for submission.
Has a "Forgot password?" link and a "Sign up" link for new users.
2. Sign-up Screen:
Facilitates new users to create an account by entering necessary details like email, username, and password.
Provides validation feedback on input fields.
After successful sign-up, the user is redirected to the profile page.
3. Profile Screen:
Displays user information like username, profile picture, and other details.
Includes a "Get Notifications" button, which allows users to toggle notifications.
Users can edit their profile details in future updates.

## Technical Challenges Faced
* Text alignment issues: Ensuring text alignment (especially in relation to buttons and icons) was tricky when centering elements. Fine-tuning the layout parameters and using RelativeLayout helped fix this.
* Image not displaying on the device: There were initial challenges with images not appearing correctly, despite being visible in the design preview. This was solved by ensuring correct image dimensions and placing the images in the appropriate drawable folder.

## Future Plans
* Authentication Integration: Implement real-time authentication using Firebase or another backend service.
* Profile Editing: Add functionality for users to edit their profile information and upload/change profile pictures.
* More Screens: Add additional screens for shopping, categories, and more as the app grows.
* Smooth Animations: Introduce animations between screen transitions and UI interactions to enhance the user experience.
* Notification System: Integrate a real-time notification system to notify users of updates or new messages.