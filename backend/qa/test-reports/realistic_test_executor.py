#!/usr/bin/env python3
import requests
import csv
import json
import time
from datetime import datetime
import sys
import os

class RealisticQATestExecutor:
    def __init__(self, base_url="http://localhost:8080"):
        self.base_url = base_url
        self.session = requests.Session()
        self.results = []
        self.current_round = 0
        self.real_data = {
            'property_ids': [],
            'project_ids': [],
            'investor_ids': [],
            'enterprise_ids': []
        }
        
    def discover_real_data(self):
        """Discover actual IDs that exist in the system"""
        print("🔍 Discovering real data in the system...")
        
        try:
            # Get property IDs
            response = self.session.get(f"{self.base_url}/api/v1/property", timeout=10)
            if response.status_code == 200:
                properties = response.json()
                self.real_data['property_ids'] = [p['id'] for p in properties]
                print(f"   📋 Found {len(self.real_data['property_ids'])} properties: {self.real_data['property_ids'][:5]}")
            
            # Get investor IDs
            response = self.session.get(f"{self.base_url}/api/v1/investors", timeout=10)
            if response.status_code == 200:
                investors = response.json()
                self.real_data['investor_ids'] = [i['id'] for i in investors]
                print(f"   👥 Found {len(self.real_data['investor_ids'])} investors: {self.real_data['investor_ids']}")
            
            # Try to discover project and enterprise IDs by testing common patterns
            for entity_type in ['projects', 'enterprises']:
                found_ids = []
                for test_id in range(1, 6):  # Test IDs 1-5
                    try:
                        response = self.session.get(f"{self.base_url}/{entity_type}/{test_id}", timeout=5)
                        if response.status_code == 200:
                            found_ids.append(test_id)
                    except:
                        pass
                
                if entity_type == 'projects':
                    self.real_data['project_ids'] = found_ids
                    print(f"   📋 Found {len(found_ids)} projects: {found_ids}")
                else:
                    self.real_data['enterprise_ids'] = found_ids
                    print(f"   🏢 Found {len(found_ids)} enterprises: {found_ids}")
                    
        except Exception as e:
            print(f"   ⚠️ Error discovering data: {e}")
            # Set fallback IDs if discovery fails
            self.real_data = {
                'property_ids': [5, 9, 20],
                'project_ids': [1, 2, 3],
                'investor_ids': [1, 2],
                'enterprise_ids': [1, 2, 3]
            }
            print(f"   🔄 Using fallback IDs")
    
    def substitute_real_data(self, endpoint, test_case):
        """Replace placeholder IDs with real IDs from the system"""
        if test_case.get('Use Real Data') != 'Yes':
            return endpoint
            
        # Property endpoints
        if '/properties/' in endpoint and self.real_data['property_ids']:
            if '/properties/5' in endpoint:
                real_id = self.real_data['property_ids'][0]
                return endpoint.replace('/properties/5', f'/properties/{real_id}')
            elif '/properties/9' in endpoint:
                real_id = self.real_data['property_ids'][1] if len(self.real_data['property_ids']) > 1 else self.real_data['property_ids'][0]
                return endpoint.replace('/properties/9', f'/properties/{real_id}')
        
        # API property endpoints
        if '/api/v1/property/' in endpoint and self.real_data['property_ids']:
            if '/api/v1/property/5' in endpoint:
                real_id = self.real_data['property_ids'][0]
                return endpoint.replace('/api/v1/property/5', f'/api/v1/property/{real_id}')
            elif '/api/v1/property/9' in endpoint:
                real_id = self.real_data['property_ids'][1] if len(self.real_data['property_ids']) > 1 else self.real_data['property_ids'][0]
                return endpoint.replace('/api/v1/property/9', f'/api/v1/property/{real_id}')
        
        # Project endpoints
        if '/projects/' in endpoint and self.real_data['project_ids']:
            if '/projects/1' in endpoint:
                real_id = self.real_data['project_ids'][0]
                return endpoint.replace('/projects/1', f'/projects/{real_id}')
        
        # Investor endpoints
        if '/investors/' in endpoint and self.real_data['investor_ids']:
            if '/investors/1' in endpoint:
                real_id = self.real_data['investor_ids'][0]
                return endpoint.replace('/investors/1', f'/investors/{real_id}')
            elif '/investors/2' in endpoint:
                real_id = self.real_data['investor_ids'][1] if len(self.real_data['investor_ids']) > 1 else self.real_data['investor_ids'][0]
                return endpoint.replace('/investors/2', f'/investors/{real_id}')
        
        # API investor endpoints
        if '/api/v1/investors/' in endpoint and self.real_data['investor_ids']:
            if '/api/v1/investors/1' in endpoint:
                real_id = self.real_data['investor_ids'][0]
                return endpoint.replace('/api/v1/investors/1', f'/api/v1/investors/{real_id}')
            elif '/api/v1/investors/2' in endpoint:
                real_id = self.real_data['investor_ids'][1] if len(self.real_data['investor_ids']) > 1 else self.real_data['investor_ids'][0]
                return endpoint.replace('/api/v1/investors/2', f'/api/v1/investors/{real_id}')
        
        # Enterprise endpoints
        if '/enterprises/' in endpoint and self.real_data['enterprise_ids']:
            if '/enterprises/1' in endpoint:
                real_id = self.real_data['enterprise_ids'][0]
                return endpoint.replace('/enterprises/1', f'/enterprises/{real_id}')
        
        # Photo API endpoints
        if '/api/v1/photos/property/5' in endpoint and self.real_data['property_ids']:
            real_id = self.real_data['property_ids'][0]
            return endpoint.replace('/api/v1/photos/property/5', f'/api/v1/photos/property/{real_id}')
        
        return endpoint
    
    def execute_test_case(self, test_case):
        """Execute a single test case with real data"""
        test_id = test_case['Test Case ID']
        test_type = test_case['Test Type']
        original_endpoint = test_case['URL/Endpoint']
        
        # Substitute real data in endpoint
        endpoint = self.substitute_real_data(original_endpoint, test_case)
        
        result = {
            'Test Case ID': test_id,
            'Test Case Description': test_case['Test Case Description'],
            'Module': test_case['Module'],
            'Test Category': test_case['Test Category'],
            'Priority': test_case['Priority'],
            'Test Type': test_type,
            'Original Endpoint': original_endpoint,
            'Actual Endpoint': endpoint,
            'Execution Time': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
            'Status': 'FAIL',
            'Response Code': None,
            'Response Time (ms)': None,
            'Error Message': None,
            'Notes': ''
        }
        
        try:
            start_time = time.time()
            
            if test_type in ['UI', 'Functional', 'Integration']:
                # UI and functional tests - check page loads
                if endpoint.startswith('/'):
                    url = f"{self.base_url}{endpoint}"
                else:
                    url = endpoint
                    
                response = self.session.get(url, timeout=10)
                response_time = int((time.time() - start_time) * 1000)
                
                result['Response Code'] = response.status_code
                result['Response Time (ms)'] = response_time
                
                if response.status_code == 200:
                    result['Status'] = 'PASS'
                    result['Notes'] = f'Page loaded successfully'
                elif response.status_code == 404:
                    result['Status'] = 'FAIL'
                    result['Error Message'] = 'Page not found (404)'
                    result['Notes'] = 'Endpoint may not exist or data not available'
                elif response.status_code == 500:
                    result['Status'] = 'FAIL'
                    result['Error Message'] = 'Server error (500)'
                    result['Notes'] = 'Application error - check logs'
                else:
                    result['Status'] = 'FAIL'
                    result['Error Message'] = f'HTTP {response.status_code}'
                    
            elif test_type == 'API':
                # API tests
                if endpoint.startswith('/'):
                    url = f"{self.base_url}{endpoint}"
                else:
                    url = endpoint
                    
                response = self.session.get(url, timeout=10)
                response_time = int((time.time() - start_time) * 1000)
                result['Response Code'] = response.status_code
                result['Response Time (ms)'] = response_time
                
                if response.status_code in [200, 201]:
                    result['Status'] = 'PASS'
                    try:
                        data = response.json()
                        if isinstance(data, list):
                            result['Notes'] = f'API returned {len(data)} record(s)'
                        else:
                            result['Notes'] = 'API returned single record'
                    except:
                        result['Notes'] = 'API responded successfully'
                elif response.status_code == 404:
                    result['Status'] = 'FAIL'
                    result['Error Message'] = 'API endpoint not found (404)'
                    result['Notes'] = 'Endpoint may not exist or ID not found'
                else:
                    result['Status'] = 'FAIL'
                    result['Error Message'] = f'HTTP {response.status_code}'
                    
            elif test_type in ['Validation', 'Performance', 'Error Handling']:
                # Complex tests - mark as manual
                result['Status'] = 'MANUAL'
                result['Notes'] = f'{test_type} test requires manual execution'
                result['Response Time (ms)'] = 0
                
            else:
                result['Status'] = 'SKIP'
                result['Notes'] = f'Unknown test type: {test_type}'
                result['Response Time (ms)'] = 0
                
        except requests.exceptions.Timeout:
            result['Error Message'] = 'Request timeout'
            result['Notes'] = 'Server took too long to respond'
        except requests.exceptions.ConnectionError:
            result['Error Message'] = 'Connection error'
            result['Notes'] = 'Could not connect to server'
        except Exception as e:
            result['Error Message'] = str(e)
            result['Notes'] = 'Unexpected error during test execution'
            
        return result
    
    def execute_all_tests(self, test_cases):
        """Execute all test cases"""
        print(f"\n🧪 Executing {len(test_cases)} realistic test cases")
        print("=" * 60)
        
        results = []
        
        for i, test_case in enumerate(test_cases, 1):
            print(f"[{i:2d}/{len(test_cases)}] {test_case['Test Case ID']}: {test_case['Test Case Description'][:50]}...")
            
            result = self.execute_test_case(test_case)
            results.append(result)
            
            # Status indicator
            status_icon = "✅" if result['Status'] == 'PASS' else "❌" if result['Status'] == 'FAIL' else "📝" if result['Status'] == 'MANUAL' else "⏭️"
            print(f"    {status_icon} {result['Status']} ({result['Response Time (ms)']}ms)")
            
            if result['Status'] == 'FAIL' and result['Error Message']:
                print(f"       {result['Error Message']}")
            
            # Small delay to avoid overwhelming server
            time.sleep(0.1)
        
        return results
    
    def generate_report(self, results):
        """Generate CSV report"""
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        filename = f"realistic_test_execution_{timestamp}.csv"
        filepath = f"/home/matheus/claude/renovacampo/patrimonio/test-reports/{filename}"
        
        # Calculate statistics
        total_tests = len(results)
        passed = len([r for r in results if r['Status'] == 'PASS'])
        failed = len([r for r in results if r['Status'] == 'FAIL'])
        manual = len([r for r in results if r['Status'] == 'MANUAL'])
        skipped = len([r for r in results if r['Status'] == 'SKIP'])
        
        print(f"\n📊 Final Results:")
        print(f"   ✅ Passed: {passed}")
        print(f"   ❌ Failed: {failed}")
        print(f"   📝 Manual: {manual}")
        print(f"   ⏭️ Skipped: {skipped}")
        print(f"   📈 Success Rate: {(passed/total_tests*100):.1f}%")
        print(f"   💾 Report saved: {filename}")
        
        # Write CSV
        fieldnames = ['Test Case ID', 'Test Case Description', 'Module', 'Test Category', 'Priority', 
                     'Test Type', 'Original Endpoint', 'Actual Endpoint', 'Execution Time', 'Status', 
                     'Response Code', 'Response Time (ms)', 'Error Message', 'Notes']
        
        with open(filepath, 'w', newline='', encoding='utf-8') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()
            for result in results:
                writer.writerow(result)
        
        return filepath

def load_realistic_test_cases():
    """Load realistic test cases"""
    test_cases = []
    csv_path = "/home/matheus/claude/renovacampo/patrimonio/QA_REALISTIC_TEST_PLAN.csv"
    
    with open(csv_path, 'r', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            test_cases.append(row)
    
    return test_cases

def main():
    print("🚀 RenovaCampo Realistic QA Test Executor")
    print("=" * 50)
    
    # Load test cases
    print("📋 Loading realistic test cases...")
    test_cases = load_realistic_test_cases()
    print(f"   Loaded {len(test_cases)} test cases")
    
    # Initialize executor
    executor = RealisticQATestExecutor()
    
    # Check if server is running
    print(f"\n🔍 Checking server at {executor.base_url}...")
    try:
        response = executor.session.get(executor.base_url, timeout=5)
        if response.status_code == 200:
            print("   ✅ Server is running")
        else:
            print(f"   ⚠️ Server responded with status {response.status_code}")
    except:
        print("   ❌ Server is not accessible")
        print("   Please start the application first: ./mvnw spring-boot:run")
        return
    
    # Discover real data
    executor.discover_real_data()
    
    # Execute all tests
    results = executor.execute_all_tests(test_cases)
    
    # Generate report
    executor.generate_report(results)
    
    print(f"\n🎉 Realistic test execution complete!")
    print(f"💾 Reports available in: /home/matheus/claude/renovacampo/patrimonio/test-reports/")

if __name__ == "__main__":
    main()