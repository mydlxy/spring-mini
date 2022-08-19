package scan.test.impl;

import parse.annotation.Autowired;
import parse.annotation.Component;
import parse.annotation.Value;
import scan.test.MyInterface;

/**
 * @author myd
 * @date 2022/8/18  15:17
 */


@Component
public class ASD {
    @Autowired
    QWE qwe;


    @Autowired
    public MyInterface myInterface;

    @Value("asd.....tttt")
   public String t;

    @Value("${name}")
    public String name;

    public QWE getQwe(){
        return  qwe;
    }

}
