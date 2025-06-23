// npm init -y
// npm install firebase-admin
// node JsonToFirebase.js

const admin = require('firebase-admin');
const serviceAccount = require('./tp-android-feddouliwiam-firebase-adminsdk-fbsvc-ae03964e27.json');
const products = require('./products.json'); // your existing product JSON file

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

async function uploadProducts() {
  for (const product of products) {
    if (product.id && product.id.toString().trim() !== "") {
	await db.collection('products').doc(product.id.toString()).set(product);
  	console.log('Product added:', product.id);
	} else {
  	console.warn('Skipped product with invalid or missing id:', product);
	}
  }
  console.log('Upload complete');
}

uploadProducts().catch(console.error);