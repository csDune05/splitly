#!/bin/bash
#
# Android Agent Skills - Interactive Setup Wizard
# Version: 1.1.0
#
# Usage: ./scripts/setup.sh
#
# This script interactively configures IDE-specific files for your Android project.
#

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SOURCE_DIR="$(dirname "$SCRIPT_DIR")"

# GitHub repository URL for version check
GITHUB_RAW_URL="https://raw.githubusercontent.com/devtrongle/android-agent-skills/main/SKILL.md"

# Print banner
print_banner() {
    echo -e "${CYAN}"
    echo "╔═══════════════════════════════════════════════════════════╗"
    echo "║          Android Agent Skills - Setup Wizard              ║"
    echo "║          Version 1.1.0                                    ║"
    echo "╚═══════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

print_step() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

# Get local version from SKILL.md
get_local_version() {
    local skill_file="$SOURCE_DIR/SKILL.md"
    if [ -f "$skill_file" ]; then
        # macOS compatible: use sed instead of grep -oP
        sed -n 's/.*version:[[:space:]]*"\([^"]*\)".*/\1/p' "$skill_file" 2>/dev/null | head -n1 || echo "unknown"
    else
        echo "unknown"
    fi
}

# Get remote version from GitHub
get_remote_version() {
    local content=""
    if command -v curl &> /dev/null; then
        content=$(curl -s --connect-timeout 5 "$GITHUB_RAW_URL" 2>/dev/null || true)
    elif command -v wget &> /dev/null; then
        content=$(wget -q --timeout=5 -O - "$GITHUB_RAW_URL" 2>/dev/null || true)
    fi

    if [ -n "$content" ]; then
        # macOS compatible: use sed instead of grep -oP
        echo "$content" | sed -n 's/.*version:[[:space:]]*"\([^"]*\)".*/\1/p' | head -n1 || echo "unknown"
    else
        echo "unknown"
    fi
}

# Compare versions (returns 0 if v1 < v2, 1 if v1 >= v2)
version_compare() {
    local v1="$1"
    local v2="$2"

    if [ "$v1" = "$v2" ]; then
        return 1  # equal
    fi

    # Sort versions and check if v1 is first (meaning v1 < v2)
    local sorted=$(printf '%s\n%s' "$v1" "$v2" | sort -V | head -n1)
    if [ "$sorted" = "$v1" ]; then
        return 0  # v1 < v2 (update available)
    else
        return 1  # v1 >= v2
    fi
}

# Check for updates
check_for_updates() {
    echo -e "${BLUE}🔍 Checking for updates...${NC}"

    local local_version=$(get_local_version)
    local remote_version=$(get_remote_version)

    echo "   Local version:  $local_version"
    echo "   Remote version: $remote_version"
    echo ""

    if [ "$remote_version" = "unknown" ]; then
        print_warning "Could not check for updates (network issue)"
        return
    fi

    if [ "$local_version" = "unknown" ]; then
        print_warning "Could not determine local version"
        return
    fi

    if version_compare "$local_version" "$remote_version"; then
        echo -e "${YELLOW}╔═══════════════════════════════════════════════════════════╗${NC}"
        echo -e "${YELLOW}║  🆕 New version available: $remote_version (current: $local_version)${NC}"
        echo -e "${YELLOW}║  Run: git pull origin main                                ║${NC}"
        echo -e "${YELLOW}║  Or:  git clone https://github.com/devtrongle/android-agent-skills.git${NC}"
        echo -e "${YELLOW}╚═══════════════════════════════════════════════════════════╝${NC}"
        echo ""
        echo -n "Continue with current version? [Y/n]: "
        read -r CONTINUE_CHOICE
        if [[ "$CONTINUE_CHOICE" =~ ^[Nn]$ ]]; then
            echo "Please update and run again."
            exit 0
        fi
    else
        print_step "You have the latest version ($local_version)"
    fi
    echo ""
}



# Backup file/folder
create_backup() {
    local path="$1"
    if [ -e "$path" ]; then
        local backup_path="${path}.bak"
        # If .bak already exists, add timestamp
        if [ -e "$backup_path" ]; then
            backup_path="${path}.bak.$(date +%Y%m%d_%H%M%S)"
        fi
        mv "$path" "$backup_path"
        print_step "Backed up: $(basename "$path") → $(basename "$backup_path")"
    fi
}

# Main script
print_banner
check_for_updates

# Step 1: Get target path
echo -e "${BLUE}📁 Enter target project path:${NC}"
read -r TARGET_DIR

# Validate and resolve path
if [ -z "$TARGET_DIR" ]; then
    print_error "Path cannot be empty"
    exit 1
fi

# Expand ~ to home directory
TARGET_DIR="${TARGET_DIR/#\~/$HOME}"

# Convert Windows path to Unix path (Git Bash/MSYS2 compatible)
# Example: D:\Path\To\Dir -> /d/Path/To/Dir
if [[ "$TARGET_DIR" =~ ^[A-Za-z]:\\ ]]; then
    # Extract drive letter and path
    DRIVE_LETTER="${TARGET_DIR:0:1}"
    DRIVE_LETTER_LOWER=$(echo "$DRIVE_LETTER" | tr '[:upper:]' '[:lower:]')
    REST_PATH="${TARGET_DIR:3}"
    # Convert backslashes to forward slashes
    REST_PATH="${REST_PATH//\\//}"
    TARGET_DIR="/$DRIVE_LETTER_LOWER/$REST_PATH"
    print_warning "Converted Windows path to: $TARGET_DIR"
fi

if [ ! -d "$TARGET_DIR" ]; then
    print_error "Directory does not exist: $TARGET_DIR"
    exit 1
fi

# Resolve to absolute path (disable error exit temporarily for this command)
set +e
TARGET_DIR_RESOLVED="$(cd "$TARGET_DIR" 2>/dev/null && pwd)"
if [ $? -ne 0 ] || [ -z "$TARGET_DIR_RESOLVED" ]; then
    # If cd fails, use the path as-is (might already be absolute)
    print_warning "Could not resolve absolute path, using as-is: $TARGET_DIR"
    TARGET_DIR_RESOLVED="$TARGET_DIR"
fi
set -e
TARGET_DIR="$TARGET_DIR_RESOLVED"
echo ""
print_step "Target: $TARGET_DIR"
echo ""

# Step 2: Select IDE(s)
echo -e "${BLUE}🔧 Select IDE(s) to configure (comma-separated):${NC}"
echo "   1) Antigravity"
echo "   2) GitHub Copilot"
echo ""
echo -n "Your choice [1,2 or 1-2]: "
read -r IDE_CHOICE

# Parse IDE choice
INSTALL_ANTIGRAVITY=false
INSTALL_COPILOT=false

case "$IDE_CHOICE" in
    1)
        INSTALL_ANTIGRAVITY=true
        ;;
    2)
        INSTALL_COPILOT=true
        ;;
    "1,2"|"2,1"|"1-2"|"12"|"both"|"all")
        INSTALL_ANTIGRAVITY=true
        INSTALL_COPILOT=true
        ;;
    *)
        print_error "Invalid choice: $IDE_CHOICE"
        exit 1
        ;;
esac

echo ""

# Step 3: Ask about replacing files
REPLACE_FILES=false
echo -e "${YELLOW}⚠️  Replace existing config files? (old files will be backed up as .bak)${NC}"
echo -n "   [y/N]: "
read -r REPLACE_CHOICE

if [[ "$REPLACE_CHOICE" =~ ^[Yy]$ ]]; then
    REPLACE_FILES=true
fi

echo ""
echo -e "${BLUE}Installing...${NC}"
echo ""

# Install Antigravity config
if [ "$INSTALL_ANTIGRAVITY" = true ]; then
    AGENT_SRC="$SOURCE_DIR/.agent"
    AGENT_DEST="$TARGET_DIR/.agent"
    
    if [ -d "$AGENT_SRC" ]; then
        if [ -d "$AGENT_DEST" ]; then
            if [ "$REPLACE_FILES" = true ]; then
                create_backup "$AGENT_DEST"
                cp -r "$AGENT_SRC" "$AGENT_DEST"
                print_step "Copied: .agent/"
            else
                print_warning "Skipped: .agent/ (already exists, use replace option)"
            fi
        else
            cp -r "$AGENT_SRC" "$AGENT_DEST"
            print_step "Copied: .agent/"
        fi
    fi
fi

# Install GitHub Copilot config
if [ "$INSTALL_COPILOT" = true ]; then
    GITHUB_SRC="$SOURCE_DIR/.github"
    GITHUB_DEST="$TARGET_DIR/.github"
    COPILOT_FILE="copilot-instructions.md"
    
    if [ -d "$GITHUB_SRC" ]; then
        mkdir -p "$GITHUB_DEST"
        
        if [ -f "$GITHUB_DEST/$COPILOT_FILE" ]; then
            if [ "$REPLACE_FILES" = true ]; then
                create_backup "$GITHUB_DEST/$COPILOT_FILE"
                cp "$GITHUB_SRC/$COPILOT_FILE" "$GITHUB_DEST/$COPILOT_FILE"
                print_step "Copied: .github/$COPILOT_FILE"
            else
                print_warning "Skipped: .github/$COPILOT_FILE (already exists, use replace option)"
            fi
        else
            cp "$GITHUB_SRC/$COPILOT_FILE" "$GITHUB_DEST/$COPILOT_FILE"
            print_step "Copied: .github/$COPILOT_FILE"
        fi
    fi
fi

# Always replace agent-skills folder
SKILLS_DEST="$TARGET_DIR/agent-skills"

if [ -d "$SKILLS_DEST" ]; then
    rm -rf "$SKILLS_DEST"
fi

mkdir -p "$SKILLS_DEST"

# Copy SKILL.md
cp "$SOURCE_DIR/SKILL.md" "$SKILLS_DEST/SKILL.md"

# Copy README.md
cp "$SOURCE_DIR/README.md" "$SKILLS_DEST/README.md"

# Copy ARCHITECTURE.md
if [ -f "$SOURCE_DIR/ARCHITECTURE.md" ]; then
    cp "$SOURCE_DIR/ARCHITECTURE.md" "$SKILLS_DEST/ARCHITECTURE.md"
fi

# Copy Cheat Sheets
for cheat in "$SOURCE_DIR"/*_CHEAT_SHEET.md; do
    if [ -f "$cheat" ]; then
        cp "$cheat" "$SKILLS_DEST/$(basename "$cheat")"
    fi
done

# Copy skills folder
if [ -d "$SOURCE_DIR/skills" ]; then
    cp -r "$SOURCE_DIR/skills" "$SKILLS_DEST/skills"
fi

print_step "Replaced: agent-skills/ (latest version)"

echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}Installation complete!${NC}"
echo ""
echo "Installed:"
[ "$INSTALL_ANTIGRAVITY" = true ] && echo "  ✓ Antigravity (.agent/)"
[ "$INSTALL_COPILOT" = true ] && echo "  ✓ GitHub Copilot (.github/copilot-instructions.md)"
echo "  ✓ Agent Skills (agent-skills/)"
echo ""
