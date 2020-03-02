    package com.volvo.project.tests;

import com.volvo.project.components.*;
import com.volvo.project.components.database.ConnectionToDB;
import com.volvo.project.components.openshift.SearchInfoInLogs;
import com.volvo.project.components.testsystemconnections.Xray;
import com.volvo.project.enums.DataBaseSelection;
import com.volvo.project.enums.PathToSystemLogs;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class TestSuiteExampleTest extends TestBase {

   /* @BeforeClass
    public static void prepareDataBase() throws ClassNotFoundException, SQLException, IOException {
        logger.info("starting sql injection");
        DataSource dataSource = prepareDataSource();
        SqlUtils sqlUtils = new SqlUtils();
        final String TESTDATA_DELETE_SQL = "./src/test/resources/ExampleOfSqlScriptYouWantToRun.sql";
        sqlUtils.executeSqls(dataSource, new String[] { TESTDATA_DELETE_SQL });
        logger.info("sql script injection finished");
    }

    public static DataSource prepareDataSource() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@" + PageObject.INTERNET_DB;
        String user = PageObject.INTERNET_DB_INTERNETDBA_USER;
        String pwd = PageObject.INTERNET_DB_INTERNETDBA_PASS;
        final Connection connection = DriverManager.getConnection(url, user, pwd);
        DataSource dataSource = new MockedDataSource(connection);
        return dataSource;
    }*/
        //private CompareImages compImg = new CompareImages()

/*//    @Test(groups = {"MQintegration","smoke"})
    @Xray(test = "INTERNET-1", labels = "Label1")
    public void sendingMessagesViaMQExplorer() throws IOException, ParseException {
        //when: "sending the Test message via MQ"
        new ConnectMQ();
        String messagePath = "incoming_xml_messages/deleteDealerNewBRANDBAfield.txt";
        ConnectMQ.sendMQ(ManagerName.GimliA1, QueueName.DealerCTDIQueue, messagePath);
        //then:
        ConnectMQ.receiveMQ(ManagerName.GimliA1, QueueName.DealerCTDIQueue);
        SearchInfoInLogs searchInfoInLogs = new SearchInfoInLogs();
        String a1 = "CTDIGatewayDealerRequestProcessor";
        searchInfoInLogs.searchingInformationInLogs(a1);
    }*/


//    @Test(groups = {"database","regression"})
    @Xray(test = "INTERNET-2", labels = "Label2")
    public void connectingToDataBase() throws SQLException {
        // when: "connecting with DB"
        ConnectionToDB connectionToDB = new ConnectionToDB(DataBaseSelection.DataBaseNumber1);
        connectionToDB.connect();
        ResultSet rs = connectionToDB.executeQuery("select * from adm_user where id = 'A049473'");
        String userID = "";
        String userName = "";
        String userSurname = "";
        while (rs.next()) {
            userID = rs.getString(1);
            userName = rs.getString(2);
            userSurname = rs.getString(3);
//            float price = rs.getFloat(3);
//            int sales = rs.getInt(4);
//            int total = rs.getInt(5);
        }
        //then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(userID).isEqualTo("A049473").describedAs("incorrect userID");
        softly.assertThat(userName).isEqualTo("Paweł").describedAs("incorrect username");
        softly.assertThat(userSurname).isEqualTo("Piesniewski").describedAs("incorrect usersurname");
        softly.assertAll();
    }

    @Test(groups = {"searchInfo","smoke"})
    @Xray(test = "TCOETA-111", labels = "Label3")
    public void searchingInformationInLogs() throws IOException, ParseException {

        SearchInfoInLogs searchInfoInLogs = new SearchInfoInLogs();
        searchInfoInLogs.searchingInformationInLogs(PathToSystemLogs.SYSTEM_LOGS_QA.toString(),"c.v.b.u.b.b.");
    }
}
