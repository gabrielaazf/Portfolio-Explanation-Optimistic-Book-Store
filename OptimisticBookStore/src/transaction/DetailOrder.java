package transaction;
public class DetailOrder {
    private int orderId, bookSku, qty;

    public DetailOrder(int orderId, int bookSku, int qty) {
        this.orderId = orderId;
        this.bookSku = bookSku;
        this.qty = qty;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBookSku() {
        return this.bookSku;
    }

    public void setBookSku(int bookSku) {
        this.bookSku = bookSku;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    
}
