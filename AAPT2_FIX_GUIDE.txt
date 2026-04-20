# Fixing AAPT2 Daemon Startup Issues

If you encounter the error `AAPT2 aapt2-x.x.x-linux Daemon #0: Daemon startup failed` during a build, it is usually because the default AAPT2 binary included with the Android Gradle Plugin is incompatible with your environment (common in Termux or certain Linux distributions).

## The Fix

1. **Locate a compatible AAPT2 binary** on your system. In Termux, this is typically found at:
   `/data/data/com.termux/files/usr/bin/aapt2`

2. **Update your `gradle.properties` file** (found in the project root) to point to this binary. Add the following lines:

   ```properties
   # Use the system-installed AAPT2 instead of the one from Maven
   android.aapt2FromMavenOverride=/data/data/com.termux/files/usr/bin/aapt2


DO NOT USE MAKE_PR.SH WHILE AAPT2 FIXES ARE IN REMOVE THE FIXES TO USE MAKE_PR.SH
   android.aapt2.executable=/data/data/com.termux/files/usr/bin/aapt2
   
   # Additional flags that help in restricted environments
   android.aapt2FromMaven=true
   android.aapt.explicit=true
   ```

## Important Note for CI/CD
**DO NOT commit these absolute paths to the repository** if you are using GitHub Actions or other CI tools, as they use different file structures. 

Instead, keep these settings in your `local.properties` file or keep them as local-only changes in `gradle.properties`.

## Installation (Termux)
If you don't have AAPT2 installed in Termux, you can get it via:
```bash
pkg install aapt
```
