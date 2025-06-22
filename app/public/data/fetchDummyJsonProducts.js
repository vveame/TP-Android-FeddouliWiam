import fs from 'fs/promises';

async function fetchCategoryProducts(category) {
  const res = await fetch(`https://dummyjson.com/products/category/${category}`);
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  const { products } = await res.json();
  return products;
}

async function main() {
  try {
    const categories = [
      'beauty',
      'fragrances',
      'skin-care',
      'womens-tops',
      'womens-dresses',
      'womens-shoes',
      'womens-bags',
      'womens-watches',
      'womens-jewellery'
    ];

    console.log('⏳ Fetching products...');
    let all = [];

    for (const cat of categories) {
      console.log(`➡️ ${cat}`);
      const prods = await fetchCategoryProducts(cat);
      all = all.concat(prods);
    }

    console.log(`✅ Fetched ${all.length} total items`);

    const mapped = all.map(d => ({
      id: String(d.id),
      title: d.title,
      description: d.description ?? null,
      price: Number(d.price),
      discountPercentage: d.discountPercentage ?? null,
      rating: d.rating ?? null,
      stock: d.stock,
      restockDate: null,
      brand: d.brand ?? '',
      category: d.category,
      thumbnail: d.thumbnail,
      images: Array.isArray(d.images) ? d.images : []
    }));

    await fs.writeFile('products.json', JSON.stringify(mapped, null, 2), 'utf-8');
    console.log('✅ Saved products.json');
  } catch (e) {
    console.error('❌', e);
    process.exitCode = 1;
  }
}

main();
