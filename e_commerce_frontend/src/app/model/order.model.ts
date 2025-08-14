import { CartItem } from "./cart.model";

export interface CheckoutRequest {
  shippingAddressId: number;
  paymentMethod: string;
  notes?: string;
}

export interface OrderItem {
  productId: number;
  productName: string;
  productImage: string;
  price: number;
  quantity: number;
  subtotal: number;
}

export interface ShippingAddress {
  doorNo: string;
  street: string;
  city: string;
  district: string;
  state: string;
}

export interface PaymentInfo {
  method: string;
  status: string;
  transactionId?: string;
}

export interface OrderResponse {
  orderId: number;
  orderNumber: string;
  status: string;
  totalAmount: number;
  orderDate: string;
  items: OrderItem[];
  shippingAddress: ShippingAddress;
  paymentInfo: PaymentInfo;
}

export interface BuyNowRequest {
  cartItem: CartItem;
  checkoutRequest: CheckoutRequest;
}