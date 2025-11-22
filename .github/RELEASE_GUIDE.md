# GitHub Release Automation Guide

This guide explains how the automated release workflow builds and publishes APK files to GitHub Releases.

## Overview

The release workflow is fully automated and triggers when you push a version tag (e.g., `v1.3.5`). It will:
1. Build the signed release APK
2. Create a GitHub Release automatically
3. Attach the APK to the release
4. Generate release notes from commits

## Prerequisites

Before using the automated release workflow, ensure you have configured the following GitHub repository secrets:

### Required Secrets

1. **SIGNING_KEY_BASE64** - Your Android keystore file encoded in Base64
2. **KEY_STORE_PASSWORD** - Password for the keystore
3. **KEY_PASSWORD** - Password for the key
4. **KEY_ALIAS** - Alias name of the key
5. **DANDANPLAY_APP_ID** - DanDanPlay API App ID (optional)
6. **DANDANPLAY_APP_SECRET** - DanDanPlay API App Secret (optional)

### How to Set Up Secrets

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add each secret with its corresponding value

### Generating SIGNING_KEY_BASE64

If you need to encode your keystore file:

```bash
base64 -i keystore.jks | tr -d '\n' > keystore_base64.txt
```

Then copy the contents of `keystore_base64.txt` and add it as the `SIGNING_KEY_BASE64` secret.

## How to Create a Release

### Automatic Release (Recommended)

1. **Update Version**: Edit `app/build.gradle.kts` and update:
   ```kotlin
   versionCode = 36  // Increment this
   versionName = "1.3.5"  // Update version name
   ```

2. **Commit Changes**:
   ```bash
   git add app/build.gradle.kts
   git commit -m "Bump version to 1.3.5"
   git push origin main
   ```

3. **Create and Push Tag**:
   ```bash
   git tag v1.3.5
   git push origin v1.3.5
   ```

4. **Wait for Workflow**: The GitHub Actions workflow will automatically:
   - Build the signed release APK
   - Create a GitHub Release with tag `v1.3.5`
   - Attach the APK file to the release
   - Generate release notes from your commits

5. **Check Release**: Go to `https://github.com/lam-tom/Animius/releases` to see your new release!

### Manual Trigger (Optional)

You can also manually trigger the workflow:

1. Go to **Actions** tab in your repository
2. Select **Build and Release APK** workflow
3. Click **Run workflow**
4. Select the branch/tag
5. Click **Run workflow** button

## Release Workflow Details

### Workflow File Location
`.github/workflows/release.yml`

### Trigger Conditions
- **Automatic**: Pushes to tags matching `v*` (e.g., v1.0.0, v2.1.3)
- **Manual**: Via GitHub Actions UI (workflow_dispatch)

### Build Process
1. Checkout code
2. Set up JDK 17
3. Set up Android SDK
4. Configure Gradle
5. Decode signing key from secrets
6. Build release APK with signing
7. Upload APK as artifact
8. Create GitHub Release with APK attached

### Output
- **APK Location**: `app/build/outputs/apk/release/Animius-v{version}-release.apk`
- **Release**: Created at `https://github.com/lam-tom/Animius/releases/tag/v{version}`

## Troubleshooting

### Build Fails
- Check that all required secrets are configured correctly
- Verify the keystore password and alias match your keystore file
- Check the workflow logs in the Actions tab for specific errors

### Release Not Created
- Ensure you're pushing a tag that starts with `v` (e.g., `v1.3.5`)
- Verify `GITHUB_TOKEN` has necessary permissions (default token should work)
- Check workflow logs for permission errors

### APK Not Signed
- Verify `SIGNING_KEY_BASE64` is correctly encoded
- Check that keystore passwords are correct
- Ensure the keystore file (`keystore.jks`) exists at the root level

## Example Release Process

```bash
# 1. Update version in app/build.gradle.kts
# Change versionCode = 36 and versionName = "1.3.5"

# 2. Commit the version change
git add app/build.gradle.kts
git commit -m "Release version 1.3.5"

# 3. Create annotated tag with release notes
git tag -a v1.3.5 -m "Release v1.3.5

Features:
- Fixed URL encoding for GirigiriSource search
- Improved search with non-ASCII characters

Bug Fixes:
- Search now works with Chinese, Japanese, and Korean characters"

# 4. Push changes and tag
git push origin main
git push origin v1.3.5

# 5. Wait 5-10 minutes for the workflow to complete
# 6. Check releases page: https://github.com/lam-tom/Animius/releases
```

## Best Practices

1. **Semantic Versioning**: Use semantic versioning (e.g., v1.2.3)
   - MAJOR version for incompatible API changes
   - MINOR version for new functionality
   - PATCH version for bug fixes

2. **Annotated Tags**: Use annotated tags with release notes:
   ```bash
   git tag -a v1.3.5 -m "Release notes here"
   ```

3. **Test First**: Always test the app thoroughly before creating a release

4. **Changelog**: Keep a CHANGELOG.md file updated with changes

5. **Version Consistency**: Ensure tag version matches `versionName` in `build.gradle.kts`

## CI/CD Pipeline

### Debug Builds (Pull Requests)
The `android_ci.yml` workflow builds debug APKs for:
- Pull requests
- Pushes to `main` and `feat-compat-tv` branches

### Release Builds (Tags)
The `release.yml` workflow builds signed release APKs for:
- Tags starting with `v*`
- Manual workflow dispatch

## Support

If you encounter issues with the automated release process:
1. Check the GitHub Actions logs
2. Verify all secrets are configured
3. Test building locally: `./gradlew assembleRelease`
4. Review the workflow file for any misconfigurations
