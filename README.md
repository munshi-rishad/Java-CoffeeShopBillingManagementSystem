# ☕ Coffee Shop Billing Management System

A small desktop app made with **Java Swing**. A customer can create an account, log in,
build a coffee (base, type, brew, sugar), see the price and pay with bKash, a card,
or cash on delivery.

There is **no database**. Accounts and orders are saved in two plain text files
inside a `data` folder, which the app creates by itself.

---

## Features

- Register and log in (accounts are saved in `data/users.txt`)
- Build your coffee: Base, Type, Brew Type, Sugar
- Automatic price calculation (`Total = Base price + Brew price`)
- Three payment options: **bKash**, **Credit/Debit card**, **Cash on Delivery**
- Simple input checks (email format, 11-digit mobile number, card number, MM/YY date, CVV)
- Receipt after payment, every order saved in `data/orders.txt`
- Option to make another coffee after ordering

### Menu and prices

| Base        | Price    | Brew       | Price    |
|-------------|----------|------------|----------|
| Water       | 0.00 Tk  | Espresso   | 25.00 Tk |
| Milk        | 15.00 Tk | Cappuccino | 35.00 Tk |
| Vegan Milk  | 25.00 Tk | Americano  | 15.00 Tk |
|             |          | Latte      | 55.00 Tk |

Type (Hot / Cold / Iced) and Sugar (Yes / No) do not change the price.

---

## What you need to install

| Software | Why | Download |
|----------|-----|----------|
| **JDK 8 or newer** (JDK 17 or 21 recommended) | to compile and run Java | [Adoptium Temurin](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| **Git** (optional) | to clone the project | [git-scm.com](https://git-scm.com/downloads) |
| **An IDE** (optional) | IntelliJ IDEA, Eclipse, NetBeans or VS Code with the "Extension Pack for Java" | — |

Check that Java is installed by opening a terminal / Command Prompt and typing:

```bash
java -version
javac -version
```

Both commands should print a version number. If `javac` is "not recognized" on Windows,
add the JDK `bin` folder (for example `C:\Program Files\Java\jdk-21\bin`) to the `PATH`
environment variable and open a new Command Prompt.

---

## Setup and run

### 1. Get the project

```bash
git clone https://github.com/munshi-rishad/Java-CoffeeShopBillingManagementSystem.git
cd Java-CoffeeShopBillingManagementSystem
```

(or download the ZIP from GitHub and extract it)

> **Important:** always run the program from the **project root folder** (the folder that
> contains `src` and `assets`). The images are loaded from `assets/...` and the data is
> saved to `data/...` using paths relative to this folder. If the images are missing, the
> app shows a warning when it starts.

### 2a. Run from the terminal (Windows, macOS, Linux)

```bash
# compile all classes into the "out" folder
javac -d out src/*.java

# run (from the project root)
java -cp out Start
```

On Windows Command Prompt the same commands work:

```bat
javac -d out src\*.java
java -cp out Start
```

### 2b. Run from IntelliJ IDEA

1. **File → Open** and choose the project folder.
2. Right-click the `src` folder → **Mark Directory as → Sources Root**.
3. **File → Project Structure → Project** and pick an installed SDK (JDK 8+).
4. Open `src/Start.java` and press the green ▶ Run button.
   (The working directory is the project root by default. If you changed it, set it back in
   **Run → Edit Configurations → Working directory**.)

### 2c. Run from Eclipse

1. **File → New → Java Project**, untick "Use default location" and browse to the project folder.
2. Make sure `src` is a source folder (right-click → **Build Path → Use as Source Folder**).
3. Right-click `Start.java` → **Run As → Java Application**.

### 2d. Run from VS Code

1. Install the **Extension Pack for Java**.
2. **File → Open Folder** and choose the project folder.
3. Open `src/Start.java` and click **Run** above `main`.

---

## How to use

1. **Register** – click *Register*, fill in email, name, password, confirm password and gender,
   then *Create account*.
2. **Login** – type the same email and password and click *Login* (or press Enter).
3. **Make My Coffee** – choose Base, Type, Brew Type and Sugar. Click *Price* to see the total,
   then *Proceed*.
4. **Payment** – choose bKash, Credit/Debit or Cash on Delivery, type your location and the
   payment details, then click *Pay … Tk*. A receipt is shown and the order is saved.

The back arrow (top-left of the payment screen) returns to the coffee screen.

---

## Where the data is saved

The `data` folder is created next to `src` the first time someone registers.

| File | One line looks like |
|------|---------------------|
| `data/users.txt`  | `rishad@mail.com \| Rishad \| Male \| 03ac6742…(password hash)` |
| `data/orders.txt` | `2026-10-22 10:24 \| rishad@mail.com \| Milk \| Cold \| Latte \| Sugar \| 70.00 Tk \| bKash (01712345678) \| House 12, Road 5, Mymensingh` |

- Passwords are saved only as a **SHA-256 hash**, never as plain text.
- Card number, CVV and bKash PIN are **never saved** (only the last 4 card digits).
- To reset everything, just delete the `data` folder.
- `data/` is listed in `.gitignore`, so your test accounts are not pushed to GitHub.

---

## Project structure

```
Java-CoffeeShopBillingManagementSystem/
├── src/
│   ├── Start.java          # main() – opens the Login screen
│   ├── Login.java          # login screen
│   ├── Register.java       # create-account screen
│   ├── FrameIntro.java     # "Make My Coffee" screen and price calculation
│   ├── PaymentMethod.java  # payment screen, receipt
│   └── DataStore.java      # reads/writes data/users.txt and data/orders.txt
├── assets/
│   ├── backgrounds/        # background picture of each screen
│   └── icons/              # bKash, card, location and back icons
├── screenshots/            # original screenshots
│   └── updated/            # screenshots after the latest update
└── data/                   # created automatically (accounts and orders)
```

---

## Screenshots

### Original

| Login | Registration |
|-------|--------------|
| ![Login](screenshots/login_page.png) | ![Registration](screenshots/registration_page.png) |

| Coffee Order | Payment |
|--------------|---------|
| ![Coffee order](screenshots/coffee_order_page.png) | ![Payment](screenshots/payment_page.png) |

### After the update

What changed on screen: the total is shown with two decimals (`70.00Tk`), the payment
button now shows the amount to pay (`Pay 70.00 Tk`) with dark, readable text, and the window
title says "Payment Method". The Login and Registration screens look the same as before.
(These were captured on Linux, so some fonts look slightly different from Windows.)

| Login | Registration |
|-------|--------------|
| ![Login](screenshots/updated/login_page.png) | ![Registration](screenshots/updated/registration_page.png) |

| Coffee Order | Payment |
|--------------|---------|
| ![Coffee order](screenshots/updated/coffee_order_page.png) | ![Payment](screenshots/updated/payment_page.png) |

---

## Changes in this update

**Bug fixes**
- Old windows were only hidden (`setVisible(false)`), so every Back/Login click left another
  window in memory. They are now closed with `dispose()`.
- Logging in before registering showed "Incorrect Email!". It now says no account was found.
- On the payment screen, clicking *Confirm* first disabled every field and then turned some
  back on. The logic is rewritten so only the chosen method's fields are enabled.
- Removed an unused test method (`Exception()`) from `Login`.
- Background images were re-loaded on every repaint; now they are loaded once.
- Fixed spelling: `PaymentMathod` → `PaymentMethod`, "Customoization" → "Customization".

**Small improvements**
- Accounts and orders are saved to text files (`DataStore.java`), so more than one account
  works and nothing is lost when the app is closed.
- Windows open in the centre of the screen, can't be resized (the layout uses fixed positions),
  and buttons show a hand cursor.
- Receipt after payment, with the option to order again.
- Code is reorganised with comments and small helper methods so it is easier to read.
