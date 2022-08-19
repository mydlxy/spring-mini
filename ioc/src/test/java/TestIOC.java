import context.ApplicationContext;
import context.XmlApplicationContext;
import org.junit.Test;
import rr.qwe.wer.werwer.X;
import rr.qwe.wer.werwer.Y;
import scan.test.WSX;
import scan.test.impl.ASD;
import scan.test.impl.QWE;

import java.sql.SQLOutput;

/**
 * @author myd
 * @date 2022/8/18  0:07
 */

public class TestIOC {

    public QWE qwe;

    @Test
    public void test1() throws NoSuchFieldException {

        System.out.println(TestIOC.class.getField("qwe").getType().getTypeName());

    }

    @Test
    public void test() throws Throwable {
        ApplicationContext context = new XmlApplicationContext("spring.xml");
        ASD asd = (ASD) context.getBean(ASD.class);
        QWE q1 = asd.getQwe();
        QWE qwe = (QWE) context.getBean(QWE.class);
        System.out.println(q1==qwe);
        System.out.println(asd.t+";name:"+asd.name);

        System.out.println("myInterface:"+asd.myInterface);

//        System.out.println(asd);
    }

}
