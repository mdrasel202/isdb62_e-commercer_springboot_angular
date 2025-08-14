export interface ProductDiscountRequest{
    name:string;
    percentage:number;
    startDate: Date;
    endDate: Date;
    active: boolean;
}

export interface ProductDiscountResponse{
    id: number;
    name: string;
    percentage: number;
    startDate: Date;
    endDate: Date;
    active: boolean;
    productId: number;
}