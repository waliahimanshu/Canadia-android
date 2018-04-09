// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();


exports.sendNotification = functions.database.ref('/ee_crs/2017/{pushId}')
    .onWrite(event => {
        const data = event.data.current.val();
        const crsScore = data.crs_score;
        const itaIssued = data.ita_issues;
        const promises = [];



        return Promise.all([crsScore, itaIssued]).then(results => {
            const crsScore = results[0];
            const itaIssued = results[1];
            console.log('notifying ' + crsScore + ' with ita issued ' + itaIssued);

            const payload = {
                notification: {
                    title: 'New Draw Today',
                    body: 'Today CRS is ' + crsScore,
//                    icon: sender.photoURL
                }
            };

            admin.messaging().sendToDevice(instanceId, payload)
                .then(function (response) {
                    console.log("Successfully sent message:", response);
                })
                .catch(function (error) {
                    console.log("Error sending message:", error);
                });
        });
    });