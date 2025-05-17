## **App Odessey PUCon 2025**

## **1\. Overview**

**App Name:** Educatro  
 **Purpose:** A mobile learning platform where users can discover, subscribe to, and consume educational content.

---

## **2\. Architecture & Technology**

Participants may choose **any** programming language, framework, and backend solution.  
 Examples include (but are not limited to):

* Frontend: React Native, Flutter, Swift, Kotlin, etc.

* Backend: Firebase, Node.js, Django, Ruby on Rails, serverless functions, etc.

* Data storage: SQL, NoSQL, in‑memory cache, flat files, etc.

* Media & streaming: native video/audio players, third‑party SDKs, CDNs.

Your solution should be **scalable**, **robust**, and include **offline** support (e.g., local caching or persistence).

---

## **3\. Navigation & Theming**

* **Bottom Tab Bar** (persistent across the app) with five sections:

  1. **Browse**

  2. **My Courses**

  3. **Downloads**

  4. **Bookmarks**

  5. **Account / Settings**

* **Theming:** support **light** and **dark** modes.

* **Splash & Branding:** display an app logo or animation on launch before navigating to authentication or main flow.

## **4\. Core Screens & User Flows**

### **4.1 Splash Screen**

* Display logo/branding element.

* Automatically transition to the next screen after a short delay.

### **4.2 Authentication**

* **Sign In**, **Sign Up**, **Password Reset** flows.

* Persist user sessions securely.

* Use any auth mechanism: email/password, OAuth, SSO, etc.

### **4.3 Browse (Content Catalog)**

* Display a list or grid of available courses/content.

* Provide search and filtering by category, skill level, etc.

* Tapping an item opens its **Detail** view.

### **4.4 Content Detail**

* Show title, description, curriculum/modules, author info.

* Actions:

  * **Subscribe / Enroll**

  * **Bookmark** for later

  * **Download** for offline access

### **4.5 Subscription & Checkout**

* Present pricing plans or one‑time purchases.

* Integrate any payment gateway or simulate payments.

* Record enrollments in your chosen backend/data store.

### 

### **4.6 My Courses**

* List of subscribed/enrolled content.

* Resume playback or reading where left off.

### **4.7 Downloads**

* List of downloaded assets for offline use.

* Provide progress indicators and removal options.

### **4.8 Bookmarks**

* List of bookmarked items (courses, articles, videos).

* Allow quick access and management.

### **4.9 Notifications**

* In‑app notifications for updates, new content, announcements.

* Display a badge/count on the Notifications icon.

### **4.10 Account & Settings**

* Display profile information (name, email, avatar).

* Allow editing profile details, preferences, and app settings.

* Settings may include:

  * Notification preferences

  * Theme toggle (light/dark)

  * Language selection

  * Privacy and sign‑out

## 

## 

## **5\. Data Model**

Define your own schema. At minimum include the following collections/tables (or equivalents):

* **Users:** credentials and profile data

* **Content / Courses:** metadata, media URLs, pricing

* **Enrollments:** tracks which user is subscribed to what

* **Bookmarks:** saved items per user

* **Downloads:** offline assets per user

* **Notifications:** messages targeted to users

Include any additional models needed for features like search indexing, analytics, or progress tracking.

---

## **6\. UI & UX Guidelines**

* Follow modern mobile UX best practices: clear hierarchy, accessible touch targets, and smooth transitions.

* Use a consistent design language (your choice of fonts and color palette).

* Ensure layouts adapt to different screen sizes and orientations.

---

## **7\. Security & Privacy**

* Secure authentication and session management.

* Restrict data access so users can only read/write their own data.

* Handle sensitive data (profile, payments) following best practices (encryption at rest/in transit).

## **8\. Offline Support**

* Cache key data locally so core flows work without network.

* Sync changes (e.g., bookmarks, progress) when the device reconnects.

## **9\. Hackathon Deliverables & Presentation (15 min)**

1. **Demo:** Walk through all core flows (Browse → Detail → Subscribe → My Courses → Download/Bookmarked → Settings).

2. **Offline Demo:** Show accessing downloaded content and syncing.

3. **Architecture:** Explain tech choices and data flow.

4. **Challenges & Next Steps:** Outline potential improvements (analytics, social sharing, collaborative features).

🔖 *Good luck, and happy building\!*

