package module.assistant;

/**
 * @date： 2023/10/24
 * @author: 78495
*/
public class BNFContent {

    private String text = "#BNF+IAT 1.0 UTF-8;\n" +
            "!grammar control;\n" +
            "!slot <controlPre>;\n" +
            "!slot <name>;\n" +
            "!slot <controlTo>;\n" +
            "!start <commands>;\n" +
            "<commands>:[<controlPre>][<controlTo>]<name>;\n" +
            "<controlPre>:我要;\n" +
            "<controlTo>:打开!id(100000)|返回!id(100002);\n" +
            "<name>:导航!id(100001)|电话本!id(100003)|桌面!id(100002)|主界面!id(100002);";
}
