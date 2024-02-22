function initialize


 async function registerUser(userData) {
     const response = await fetch(`http://localhost:8080/user`, {
         method: "POST",
         headers: { "Content-Type": "application/json" },
         body: JSON.stringify(userData)
     });

     if (!response.ok) {
         throw new Error('Network response was not ok');
     }

     return response.json();
 }