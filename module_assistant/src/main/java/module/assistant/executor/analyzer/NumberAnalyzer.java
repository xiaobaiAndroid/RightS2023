package module.assistant.executor.analyzer;

import module.assistant.SiriCommandIds;

/**
 * 数字分析器
 * @date： 2023/10/30
 * @author: 78495
*/
public class NumberAnalyzer {

    public static final int UNKNOWN = -1;

    public int analysis(String id){
        int number = UNKNOWN;
        if(SiriCommandIds.NUMBER_0.equals(id)){
            number = 0;
        }else if(SiriCommandIds.NUMBER_1.equals(id)){
            number = 1;
        }else if(SiriCommandIds.NUMBER_2.equals(id)){
            number = 2;
        }else if(SiriCommandIds.NUMBER_3.equals(id)){
            number = 3;
        }else if(SiriCommandIds.NUMBER_4.equals(id)){
            number = 4;
        }else if(SiriCommandIds.NUMBER_5.equals(id)){
            number = 5;
        }else if(SiriCommandIds.NUMBER_6.equals(id)){
            number = 6;
        }else if(SiriCommandIds.NUMBER_7.equals(id)){
            number = 7;
        }else if(SiriCommandIds.NUMBER_8.equals(id)){
            number = 8;
        }else if(SiriCommandIds.NUMBER_9.equals(id)){
            number = 9;
        }else if(SiriCommandIds.NUMBER_10.equals(id)){
            number = 10;
        }else if(SiriCommandIds.NUMBER_11.equals(id)){
            number = 11;
        }else if(SiriCommandIds.NUMBER_12.equals(id)){
            number = 12;
        }else if(SiriCommandIds.NUMBER_13.equals(id)){
            number = 13;
        }else if(SiriCommandIds.NUMBER_14.equals(id)){
            number = 14;
        }else if(SiriCommandIds.NUMBER_15.equals(id)){
            number = 15;
        }else if(SiriCommandIds.NUMBER_16.equals(id)){
            number = 16;
        }else if(SiriCommandIds.NUMBER_17.equals(id)){
            number = 17;
        }else if(SiriCommandIds.NUMBER_18.equals(id)){
            number = 18;
        }else if(SiriCommandIds.NUMBER_19.equals(id)){
            number = 19;
        }else if(SiriCommandIds.NUMBER_20.equals(id)){
            number = 20;
        }else if(SiriCommandIds.NUMBER_21.equals(id)){
            number = 21;
        }else if(SiriCommandIds.NUMBER_22.equals(id)){
            number = 22;
        }else if(SiriCommandIds.NUMBER_23.equals(id)){
            number = 23;
        }else if(SiriCommandIds.NUMBER_24.equals(id)){
            number = 24;
        }else if(SiriCommandIds.NUMBER_25.equals(id)){
            number = 25;
        }else if(SiriCommandIds.NUMBER_26.equals(id)){
            number = 26;
        }else if(SiriCommandIds.NUMBER_27.equals(id)){
            number = 27;
        }else if(SiriCommandIds.NUMBER_28.equals(id)){
            number = 28;
        }
        return number;
    }

}
