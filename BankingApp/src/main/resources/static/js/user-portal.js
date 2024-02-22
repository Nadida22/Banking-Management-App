


  async function fetchInitialData() {
            const requestData = {
            let balanceUrl = 'http://localhost:8080/user/balance';
            let userInfo =

            };

            try {




                const response = await fetch('http://localhost:8080/user/balance', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(requestData),
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }

                const responseData = await response.json();
                displayData(responseData);
            } catch (error) {
                console.error('Error during fetch:', error);
            }
        }

        function displayData(data) {
            // Display data on your page
            console.log(data);
        }