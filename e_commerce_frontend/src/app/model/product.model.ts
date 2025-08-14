import { ProductDiscountResponse } from "./product-discount.model ";
import { ProductImage } from "./product-images.model";

export interface ProductResponse{
    id:number;
    name:string;
    description:string;
    price:number;
    currentPrice:number;
    quantity:number;
    category:string;
    sku:string;
    images:ProductImage[];
    discounts:ProductDiscountResponse[];
}

export interface ProcutRequest{
    name:string;
    description:string;
    price:number;
    quantity:number;
    category:string;
    sku:string;
}