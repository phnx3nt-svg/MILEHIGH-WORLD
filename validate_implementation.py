import json
import os
import sys

def validate():
    print("Beginning project implementation validation...")

    # 1. Validate campaign_master.json schema & contents
    json_path = os.path.join("app", "src", "main", "assets", "campaign_master.json")
    if not os.path.exists(json_path):
        print(f"Error: Asset file missing at {json_path}")
        sys.exit(1)

    try:
        with open(json_path, "r", encoding="utf-8") as f:
            data = json.load(f)
    except Exception as e:
        print(f"Error reading JSON asset: {e}")
        sys.exit(1)

    required_keys = ["sceneId", "metadata", "characters", "scenarios"]
    for k in required_keys:
        if k not in data:
            print(f"Error: Missing key '{k}' in campaign_master.json")
            sys.exit(1)

    scenarios = data.get("scenarios", [])
    if not isinstance(scenarios, list) or len(scenarios) == 0:
        print("Error: 'scenarios' must be a non-empty list")
        sys.exit(1)

    print(f"campaign_master.json valid with {len(scenarios)} scenarios.")

    # 2. Check required Kotlin source files exist
    required_kt_files = [
        os.path.join("app", "src", "main", "java", "com", "example", "milehigh", "MainActivity.kt"),
        os.path.join("app", "src", "main", "java", "com", "example", "milehigh", "ui", "CampaignViewModel.kt"),
        os.path.join("app", "src", "main", "java", "com", "example", "milehigh", "data", "HorizonModels.kt"),
        os.path.join("app", "src", "main", "java", "com", "example", "milehigh", "core", "SentinelSecurity.kt")
    ]

    for kt_file in required_kt_files:
        if not os.path.exists(kt_file):
            print(f"Error: Required Kotlin source file missing: {kt_file}")
            sys.exit(1)

    print("All required source files present and verified.")
    print("Project validation successful!")

if __name__ == "__main__":
    validate()
