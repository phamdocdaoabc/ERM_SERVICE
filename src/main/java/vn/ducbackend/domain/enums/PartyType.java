package vn.ducbackend.domain.enums;

public enum PartyType {
    // Bên ngoài
    CUSTOMER_B2B("Khách hàng B2B", Source.OUTSIDE),
    CUSTOMER_B2C("Khách hàng B2C", Source.OUTSIDE),
    OEM("Khách hàng OEM", Source.OUTSIDE),
    SUPPLIER("Nhà cung cấp", Source.OUTSIDE),
    SHIPPING_PARTNER("Đối tác vận chuyển", Source.OUTSIDE),
    PAYMENT_PARTNER("Đối tác thanh toán", Source.OUTSIDE),
    OTHER_PARTNER("Đối tác khác", Source.OUTSIDE),
    MERCHANT("Merchant", Source.OUTSIDE),
    COMPETITOR("Đối thủ", Source.OUTSIDE),
    INTERNAL_PARTNER("Đối tác nội bộ", Source.OUTSIDE),

    // Nội bộ
    CORPORATION("Tập đoàn", Source.INTERNAL),
    ORGANIZATION("Tổ chức", Source.INTERNAL),
    DEPARTMENT("Bộ phận", Source.INTERNAL),
    INDIVIDUAL("Cá nhân", Source.INTERNAL);

    private final String displayName;
    private final Source parent;

    PartyType(String displayName, Source parent) {
        this.displayName = displayName;
        this.parent = parent;
    }

    public Source getParent() {
        return parent;
    }

    public String getDisplayName() {
        return displayName;
    }
}
