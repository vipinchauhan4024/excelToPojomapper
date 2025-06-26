package com.vcdeveloper.excelmapper.util.constants;


public enum HighLevelFgEnum  {
    HFG_1                    ("1"),
    HFG_2                    ("2"),
    HFG_3                    ("3"),
    HFG_4                    ("4"),
    HFG_5                    ("5"),
    HFG_6                    ("6"),
    HFG_7                    ("7"),
    HFG_8                    ("8"),
    HFG_9                    ("9");
   

    /**
     * HighLevelFg .
     */
    private String highLevelFg;
    
    
    HighLevelFgEnum(String highLevelFg) {
        this.highLevelFg = highLevelFg;
    }
    
    @Override
    public String toString() {
        return highLevelFg;
    } 
    
    public static HighLevelFgEnum fromValue(String v) {
        return valueOf(v);
    }
    
    public static HighLevelFgEnum getHighLevelFgEnum (String hfg) {
        for (HighLevelFgEnum hfgEnum : HighLevelFgEnum.values()){
            if (hfgEnum.toString().equals(hfg)) {
                return hfgEnum;
            }
        }
         String msg = getErrorMsg();
         msg = hfg != null ? msg + hfg : msg;
         throw new IllegalArgumentException(msg);
    }

    public static String getErrorMsg() {
        return StringConstants.INVAILD_HIGH_LEVLE_FG;
    }
    
    
}