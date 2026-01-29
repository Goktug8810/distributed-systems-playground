import concurrent.futures
import time
import urllib.request
import json
import uuid
import argparse
import os

# Configuration
BASE_URL = os.environ.get("BASE_URL", "http://localhost:7070/api")

# Read IDs from env
PRODUCT_ID = os.environ.get("PRODUCT_ID", "00000000-0000-0000-0000-000000000001")
USER_ID = os.environ.get("USER_ID", "00000000-0000-0000-0000-000000000001")

print(f"Using BASE_URL: {BASE_URL}")
print(f"Using PRODUCT_ID: {PRODUCT_ID}")
print(f"Using USER_ID: {USER_ID}")

# Parse Arguments
parser = argparse.ArgumentParser()
parser.add_argument("--mode", default="buggy", help="Execution mode: buggy or fixed")
args = parser.parse_args()

print(f"Execution Mode: {args.mode}")

# Endpoint: /orders/create-order
URL = f"{BASE_URL}/orders/create-order?mode={args.mode}"

def create_order(index):
    payload = {
        "userId": USER_ID,
        "items": [
            {
                "productId": PRODUCT_ID,
                "quantity": 1
            }
        ]
    }
    
    data = json.dumps(payload).encode('utf-8')
    req = urllib.request.Request(URL, data=data, headers={'Content-Type': 'application/json'})
    
    try:
        with urllib.request.urlopen(req) as response:
            return response.getcode()
    except urllib.error.HTTPError as e:
        return e.code
    except Exception as e:
        return str(e)

def run_race_condition_test():
    print("Starting Race Condition Test...")
    concurrency = 150
    success_count = 0
    fail_count = 0
    errors = []

    with concurrent.futures.ThreadPoolExecutor(max_workers=50) as executor:
        # Launch 150 requests
        futures = [executor.submit(create_order, i) for i in range(concurrency)]
        
        for future in concurrent.futures.as_completed(futures):
            result = future.result()
            if result == 200:
                success_count += 1
            else:
                fail_count += 1
                if result not in errors:
                    errors.append(result)

    print(f"Test Completed.")
    print(f"Total Requests: {concurrency}")
    print(f"Success (Order Created): {success_count}")
    print(f"Failed: {fail_count}")
    print(f"Error Codes/Messages: {errors}")

    # Expected Result for BUGGY implementation:
    # Success count > 100 (assuming initial stock 100)
    
    if success_count > 100:
        print("FAIL: Race Condition detected! Sold more than stock.")
    elif success_count <= 100:
         print("PASS: Stock limit respected (or other errors occurred).")

if __name__ == "__main__":
    run_race_condition_test()
