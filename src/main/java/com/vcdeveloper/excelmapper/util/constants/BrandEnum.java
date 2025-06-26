package com.vcdeveloper.excelmapper.util.constants;
public enum BrandEnum  {
    VOLVO                    ("Volvo"),
    VTNA                     ("VTNA"),
    MACK                     ("Mack"),
    RENAULT                  ("Renault"),
    UD                       ("UD"),
    UD_LEGACY                ("UD Legacy"),
    EICHER                   ("Eicher"),
    BUS                      ("Bus");
   

    /**
     * brand .
     */
    private String brand;
    
    BrandEnum(String brand) {
        this.brand = brand;
    }
    
    @Override
    public String toString() {
        return brand;
    }    
   
    public static String getBrandsAsString(StringBuilder builder, BrandEnum... brands ) {
       if (brands == null) {
            return null;
       }
       if (builder == null) {
           builder = new StringBuilder();
       }
       for (BrandEnum brandEnum : brands) {
           builder.append(brandEnum.toString());
       }
       return builder.toString();
    }
    
    public static BrandEnum getBrandEnum (String brand) {
        
        for (BrandEnum brandEnum : BrandEnum.values()){
            if (brandEnum.toString().equals(brand)) {
                return brandEnum;
            }
        }
         String msg = getErrorMsg();
         msg = brand != null ? msg + brand : msg;
         throw new IllegalArgumentException(msg);
    }

    public static String getErrorMsg() {
        return StringConstants.INVAILD_BRAND;
    }
    
}