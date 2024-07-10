# **Chat AI**

Welcome to **Chat AI**! This Android app leverages the power of the OpenAI API and Firebase to create a basic chat application with minimal setup with GPT.Using firebase as backend for simplification

## **Features**

- **Firebase Authentication**: Secure and seamless login experience using Firebase.
- **Unlimited GPT Chats**: Engage in unlimited conversations with GPT, powered by OpenAI API.
- **Secure API Key Storage**: Your OpenAI API key is securely stored on Firebase.
- **Basic UI**: Focused on functionality with a clean and minimalistic design.

## **Getting Started**

### **Prerequisites**

**Firebase Setup**
- Create a Firebase Project
- Go to the Firebase Console and create a new project.
- Add Firebase to Your Android App

**Follow the steps to add Firebase to your Android app:**

- Download the google-services.json file and place it in the app directory.
- Add Firebase SDK dependencies to your build.gradle files.

**Enable Authentication**
In the Firebase Console, enable the desired authentication methods (e.g., Email/Password).

**Store OpenAI API Key**

Store your OpenAI API key in Firebase's Firestore in api_key -> open_ai -> key = "your key"


