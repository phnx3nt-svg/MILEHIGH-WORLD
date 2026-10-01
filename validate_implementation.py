import os
import json

def validate():
    print("Validating campaign_master.json asset...")
    json_path = "app/src/main/assets/campaign_master.json"
    if not os.path.exists(json_path):
        raise FileNotFoundError(f"Missing asset file: {json_path}")

    with open(json_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    assert "sceneId" in data, "Missing sceneId in campaign_master.json"
    assert "metadata" in data, "Missing metadata in campaign_master.json"
    assert data["metadata"].get("systemParity") == 9, "systemParity must be 9"
    print("Validation successful!")

if __name__ == "__main__":
    validate()
