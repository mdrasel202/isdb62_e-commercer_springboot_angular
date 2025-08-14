export interface CartItem {
    productId: number;
    quantity: number;
}

export interface CartItemDetail {
    productId: number;
    productName: string;
    productImage: string;
    price: number;
    discountedPrice: number;
    quantity: number;
    itemTotal: number;
}

export interface CartResponse {
    cartId: number;
    userId: number;
    items: CartItemDetail[];
    subtotal: number;
    total: number;
    discount: number;
    createdAt: string;
    updatedAt: string;
}