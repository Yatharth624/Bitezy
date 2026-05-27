/* ==========================================================================
   Bitezy JS Legal & Publishing Engine - Client-side Interactive Logic
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {
    // Initialise Feather Icons
    if (typeof feather !== 'undefined') {
        feather.replace();
    }

    // --- DOM Elements ---
    const themeToggleBtn = document.getElementById('theme-toggle');
    const tabItems = document.querySelectorAll('.tab-item');
    const tabContents = document.querySelectorAll('.tab-content');
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toast-message');
    const configForm = document.getElementById('config-form');

    // --- State Variables ---
    let activeTheme = localStorage.getItem('bitezy-theme') || 'dark';

    // --- Dynamic Binding Configs ---
    const variables = {
        appName: 'Bitezy',
        devName: 'Bitezy Team',
        supportEmail: 'support@bitezy.app',
        websiteUrl: 'https://bitezy.app',
        effectiveDate: 'May 28, 2026'
    };

    // Mapping inputs to variable keys
    const inputMap = {
        'input-app-name': 'appName',
        'input-dev-name': 'devName',
        'input-support-email': 'supportEmail',
        'input-website-url': 'websiteUrl',
        'input-effective-date': 'effectiveDate'
    };

    // --- Markdown Templates (Pre-formatted for clean copying) ---
    const templates = {
        privacy: () => `# Privacy Policy for ${variables.appName}
**Last Updated:** ${variables.effectiveDate}

This Privacy Policy explains how ${variables.devName} ("we", "us", or "our") collects, uses, discloses, and protects your information when you use ${variables.appName} (the "App"), our calorie tracker and nutrition helper mobile application.

We are committed to protecting your personal data and your privacy. By using the App, you agree to the collection and use of information in accordance with this Privacy Policy.

---

### 1. Information We Collect
To provide a tailored calorie tracking and nutrition lookup experience, ${variables.appName} collects information from you in the following ways:

#### a. Health and Fitness Data
* **Physical Profile:** We may collect your age, gender, height, weight, daily activity level, and calorie/macro goals to calculate your customized baseline daily calorie budget.
* **Calorie and Meal Logs:** We store the foods, portions, ingredients, meals, and calorie/macro values you log.
* **Step and Activity Tracking:** With your explicit permission, the App connects with platform APIs such as **Google Fit / Health Connect** (or device step sensors) to retrieve your daily step counts and active calorie burn. This information is used strictly to adjust your daily calorie goals in real-time.

#### b. Media and Camera Access
* **Food Camera Scanning:** The App offers features to detect foods using your camera or uploaded images. We access your device camera and selected image gallery **only** when you explicitly initiate a food scan. Images are sent to secure APIs for food recognition and are not saved on our servers.

#### c. Account and Contact Information
* **Profile Setup:** If you register an account, we may collect your email address and profile name. You may also use the App entirely offline or locally, in which case this data remains solely on your local device.

#### d. Usage and Device Technical Data
* **Device Info:** We may collect basic system info (operating system version, device model, and unique device identifiers) for performance optimization and debugging.

---

### 2. How We Use Your Information
We use the collected information for the following purposes:
* **Core Functionality:** To calculate your calorie budgets, store logged foods, track daily nutrition, and display your progress.
* **Activity Integration:** To incorporate step count and active calories into your daily calorie calculations.
* **AI Food Detection:** To process food pictures and estimate meals using secure image recognition APIs.
* **App Improvement:** To monitor and analyze App performance, fix crashes, and optimize user experience.

---

### 3. Third-Party Services
To provide advanced food scanning, robust nutrition databases, and analytics, we integrate the following third-party APIs:
* **LogMeal API:** Used for automated food image recognition. Images you capture are transmitted securely to LogMeal to identify dishes.
* **FatSecret API & OpenFoodFacts:** Used to resolve generic nutritional information and packaged food barcodes.
* **Google Fit / Health Connect:** Synchronizes step tracking and physical activity. Data read is processed locally and is never uploaded to external advertising systems or data brokers.
* **Google Firebase & Analytics (Optional):** Used for anonymous crash reports and basic usage metrics.

---

### 4. Data Sharing and Disclosure
We value your trust. We do **not** sell, rent, trade, or distribute your personal health information, calorie logs, or step tracking data to third-party advertisers, data brokers, or commercial systems. We only share information under the following limited circumstances:
* **With Service Providers:** To run the essential API calls (LogMeal, FatSecret) required to fetch nutrition stats and recognize images.
* **For Legal Reasons:** If required to do so by applicable law, regulation, or legal process.

---

### 5. Data Security
The security of your data is paramount. We employ industry-standard administrative, physical, and technical safeguards (such as HTTPS encryption for all API communication) to protect your information. However, please be aware that no method of transmission over the internet or mobile network is 100% secure.

---

### 6. Children's Privacy
Our App is **not** directed toward children under the age of 13 (or 16 in the European Union). We do not knowingly collect personal data from children. If we discover that a child under 13 has provided us with personal information, we will delete it immediately.

---

### 7. Data Retention and Deletion
If you use ${variables.appName} locally without an account, your data is stored strictly on your device and will be deleted if you uninstall the App. If you created a cloud account, you can request the deletion of your account and all associated calorie, health, and profile logs at any time directly through the App settings or by emailing us at ${variables.supportEmail}.

---

### 8. Changes to This Privacy Policy
We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page and updating the "Last Updated" date.

---

### 9. Contact Information
If you have any questions or suggestions regarding this Privacy Policy, please contact us at:

**${variables.devName}**
* Website: [${variables.websiteUrl}](${variables.websiteUrl})
* Email: [${variables.supportEmail}](mailto:${variables.supportEmail})`,

        terms: () => `# Terms & Conditions for ${variables.appName}
**Last Updated:** ${variables.effectiveDate}

Welcome to ${variables.appName}! By downloading, installing, or using our mobile application (the "App"), you agree to be bound by these Terms & Conditions ("Terms"). If you do not agree to these Terms, please do not use the App.

---

### 1. Purpose and Disclaimer
**Bitezy is a self-tracking calorie and nutritional helper. It is not a medical application.**
* The content provided in the App, including nutrition budgets, food calories, suggestions, and activity analysis, is for general educational and informational purposes only.
* The App is **not** a substitute for professional medical advice, diagnosis, or treatment. Always consult with a qualified physician before beginning any new diet.
* We do not guarantee the absolute accuracy of nutrition labels, calorie estimates, or AI image detection.

### 2. Intellectual Property and Proprietary Data
All intellectual property rights in the App, its design, software, code, branding, and proprietary databases (including our **custom curated Indian meal nutrition dataset**) belong exclusively to ${variables.devName}.
* You are granted a limited, personal, non-exclusive, non-transferable, and revocable license to use the App for personal, non-commercial tracking.
* You may **not** scrape, copy, export, distribute, or reverse-engineer our proprietary Indian meal dataset or any other database incorporated within the App for commercial use or integration into third-party apps.

### 3. User Accounts & Responsibilities
* If you create an account, you are responsible for maintaining the confidentiality of your login credentials.
* You agree to use the App only for lawful purposes. You must not upload images or content that violate third-party copyrights or are abusive.

### 4. Third-Party APIs and Content
Our App integrates with third-party service providers (such as LogMeal API for food recognition, OpenFoodFacts, FatSecret API, and Google Fit). We are not responsible for any downtime or inaccurate results of these third-party systems.

### 5. Limitation of Liability
To the maximum extent permitted by applicable law, ${variables.devName} shall not be liable for any indirect, incidental, special, consequential, or punitive damages resulting from your access to or use of (or inability to access or use) the App, or any health issues or medical conditions arising from your dietary changes.

### 6. Termination
We reserve the right, in our sole discretion, to terminate or suspend your access to the App at any time, without prior notice or liability, for any reason, including if you violate these Terms.

### 7. Changes to These Terms
We reserve the right to modify or replace these Terms at any time. If a revision is material, we will provide notice in the App or update the effective date.

### 8. Contact Us
If you have any questions about these Terms, please contact us at:
* Email: [${variables.supportEmail}](mailto:${variables.supportEmail})`
    };

    // --- Initialise Theme ---
    document.documentElement.setAttribute('data-theme', activeTheme);
    updateThemeIcon(activeTheme);

    themeToggleBtn.addEventListener('click', () => {
        activeTheme = activeTheme === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', activeTheme);
        localStorage.setItem('bitezy-theme', activeTheme);
        updateThemeIcon(activeTheme);
        showToast(`Switched to ${activeTheme === 'dark' ? 'Dark' : 'Light'} Mode!`);
    });

    function updateThemeIcon(theme) {
        if (themeToggleBtn && typeof feather !== 'undefined') {
            const icon = theme === 'dark' ? 'sun' : 'moon';
            themeToggleBtn.innerHTML = `<i data-feather="${icon}" class="toggle-icon"></i>`;
            feather.replace();
        }
    }

    // --- Dynamic Binding Engine ---
    function updateDOMBindings() {
        // App Name
        document.querySelectorAll('.bind-app-name').forEach(el => el.textContent = variables.appName);
        
        // Developer / Company
        document.querySelectorAll('.bind-dev-name').forEach(el => el.textContent = variables.devName);
        
        // Support Email
        document.querySelectorAll('.bind-support-email').forEach(el => el.textContent = variables.supportEmail);
        document.querySelectorAll('.bind-support-email-href').forEach(el => {
            el.href = `mailto:${variables.supportEmail}`;
        });

        // Website URL
        document.querySelectorAll('.bind-website-url').forEach(el => el.textContent = variables.websiteUrl);
        document.querySelectorAll('.bind-website-url-href').forEach(el => {
            el.href = variables.websiteUrl;
        });

        // Effective Date
        document.querySelectorAll('.bind-effective-date').forEach(el => el.textContent = variables.effectiveDate);
        
        // Synchronise hidden textareas for clipboard operations
        const privacyRawEl = document.getElementById('privacy-raw');
        if (privacyRawEl) privacyRawEl.value = templates.privacy();
        
        const termsRawEl = document.getElementById('terms-raw');
        if (termsRawEl) termsRawEl.value = templates.terms();
    }

    // Attach listeners to input fields
    if (configForm) {
        Object.keys(inputMap).forEach(inputId => {
            const inputEl = document.getElementById(inputId);
            if (inputEl) {
                inputEl.addEventListener('input', (e) => {
                    const varKey = inputMap[inputId];
                    variables[varKey] = e.target.value;
                    updateDOMBindings();
                });
            }
        });
    }

    // Initial Trigger
    updateDOMBindings();

    // --- Tab Switching Logic ---
    tabItems.forEach(item => {
        item.addEventListener('click', () => {
            const targetTab = item.getAttribute('data-tab');
            
            // Toggle Tab Item active states
            tabItems.forEach(tab => tab.classList.remove('active'));
            item.classList.add('active');

            // Toggle Content active states
            tabContents.forEach(content => {
                content.classList.remove('active');
                if (content.id === targetTab) {
                    content.classList.add('active');
                }
            });

            // Smooth scroll content container to top
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });
    });

    // --- Clipboard Copy Logic ---
    const copyButtons = document.querySelectorAll('.btn-copy');
    copyButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetId = btn.getAttribute('data-target');
            const dataType = btn.getAttribute('data-type');
            let contentToCopy = '';

            const targetEl = document.getElementById(targetId);
            if (!targetEl) return;

            if (dataType === 'markdown') {
                // If markdown copy, grab the raw template output
                contentToCopy = targetId === 'privacy-raw' ? templates.privacy() : templates.terms();
            } else if (dataType === 'text') {
                // Otherwise copy plain text content
                contentToCopy = targetEl.innerText || targetEl.textContent;
            }

            copyToClipboard(contentToCopy, () => {
                showToast(`Copied ${dataType === 'markdown' ? 'Markdown' : 'text'} successfully!`);
            });
        });
    });

    function copyToClipboard(text, callback) {
        if (navigator.clipboard && navigator.clipboard.writeText) {
            navigator.clipboard.writeText(text)
                .then(callback)
                .catch(err => {
                    console.error('Failed to copy using clipboard API: ', err);
                    fallbackCopy(text, callback);
                });
        } else {
            fallbackCopy(text, callback);
        }
    }

    function fallbackCopy(text, callback) {
        const textArea = document.createElement('textarea');
        textArea.value = text;
        textArea.style.position = 'fixed'; // Avoid scrolling to bottom
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        try {
            document.execCommand('copy');
            if (callback) callback();
        } catch (err) {
            console.error('Fallback copying failed: ', err);
        }
        document.body.removeChild(textArea);
    }

    // --- Standalone HTML File Downloader ---
    const downloadButtons = document.querySelectorAll('.btn-download');
    downloadButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetId = btn.getAttribute('data-target');
            const filename = btn.getAttribute('data-filename');
            const targetEl = document.getElementById(targetId);
            if (!targetEl) return;

            // Generate self-contained HTML template with beautiful minimal styles for offline viewing
            const docHTML = `<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${targetId === 'privacy-content' ? 'Privacy Policy' : 'Terms & Conditions'} - ${variables.appName}</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Outfit:wght@600;700;800&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            line-height: 1.7;
            color: #334155;
            background-color: #f8fafc;
            padding: 40px 20px;
            margin: 0;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: #ffffff;
            padding: 50px;
            border-radius: 12px;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05), 0 2px 4px -1px rgba(0,0,0,0.02);
            border: 1px solid #e2e8f0;
        }
        h1 {
            font-family: 'Outfit', sans-serif;
            font-size: 2.2rem;
            color: #0f172a;
            margin-top: 0;
            letter-spacing: -0.5px;
        }
        .meta-date {
            color: #64748b;
            font-size: 0.95rem;
            font-weight: 500;
            margin-bottom: 30px;
        }
        h3 {
            font-family: 'Outfit', sans-serif;
            font-size: 1.4rem;
            color: #0f172a;
            margin: 35px 0 15px 0;
            border-bottom: 1px solid #f1f5f9;
            padding-bottom: 8px;
        }
        h4 {
            font-family: 'Outfit', sans-serif;
            font-size: 1.1rem;
            color: #1e293b;
            margin: 20px 0 10px 0;
        }
        p {
            margin-bottom: 16px;
        }
        ul {
            margin-left: 20px;
            margin-bottom: 20px;
        }
        li {
            margin-bottom: 8px;
        }
        hr {
            border: 0;
            height: 1px;
            background: #e2e8f0;
            margin: 30px 0;
        }
        .contact-card {
            background: #f1f5f9;
            padding: 20px;
            border-radius: 8px;
            border: 1px solid #e2e8f0;
            display: inline-block;
            margin-top: 15px;
        }
        a {
            color: #22c55e;
            text-decoration: none;
            font-weight: 600;
        }
        a:hover {
            text-decoration: underline;
        }
        @media (max-width: 600px) {
            body { padding: 15px 10px; }
            .container { padding: 25px 15px; }
            h1 { font-size: 1.7rem; }
        }
    </style>
</head>
<body>
    <div class="container">
        ${targetEl.innerHTML}
    </div>
</body>
</html>`;

            downloadFile(docHTML, 'text/html', filename);
        });
    });

    function downloadFile(content, contentType, filename) {
        const blob = new Blob([content], { type: `${contentType};charset=utf-8;` });
        if (navigator.msSaveBlob) { // IE 10+
            navigator.msSaveBlob(blob, filename);
        } else {
            const link = document.createElement('a');
            if (link.download !== undefined) {
                const url = URL.createObjectURL(blob);
                link.setAttribute('href', url);
                link.setAttribute('download', filename);
                link.style.visibility = 'hidden';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                showToast(`Downloaded ${filename} successfully!`);
            }
        }
    }

    // --- Toast Controller ---
    let toastTimeout;
    function showToast(message) {
        if (!toast || !toastMessage) return;

        // Clear existing timeout
        clearTimeout(toastTimeout);

        toastMessage.textContent = message;
        toast.classList.add('show');

        toastTimeout = setTimeout(() => {
            toast.classList.remove('show');
        }, 2500);
    }
});
