package databasetest;

import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {

    public JSONArray getJSONData() {

        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;

        String query, key, value;
        
        JSONArray arrayJ = new JSONArray();

        boolean hasresults;
        int resultCount, columnCount, updateCount = 0;

        try {

            /* Identify the Server */

            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "CS310";
            System.out.println("Connecting to " + server + "...");

            /* Load the MySQL JDBC Driver */

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            /* Open Connection */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */

            if(conn.isValid(0)) {

                /* Connection Open! */

                System.out.println("Connected Successfully!");

                /* Prepare Select Query */

                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);

                /* Execute Select Query */

                System.out.println("Submitting Query ...");

                hasresults = pstSelect.execute();                

                /* Get Results */

                System.out.println("Getting Results ...");

                while(hasresults || pstSelect.getUpdateCount() != -1) {

                    if(hasresults) {

                        /* Get ResultSet Metadata */

                        resultset = pstSelect.getResultSet();
                        
                        metadata = resultset.getMetaData();
                        
                        columnCount = metadata.getColumnCount();

                        while(resultset.next()) {

                            /* Begin Next ResultSet Row */

                            System.out.println();

                            /* Loop Through ResultSet Columns; Print Values */

                            JSONObject jsonObj = new JSONObject();
                            
                            for(int i = 2; i <= columnCount; i++) {
                                
                                value = resultset.getString(i);
                                
                                key = metadata.getColumnLabel(i);
                                
                                jsonObj.put(key, value);

                            }
                            
                            arrayJ.add(jsonObj);
                        }
                    } else {

                        resultCount = pstSelect.getUpdateCount();  

                        if(resultCount == -1) {
                            break;
                        }
                    }

                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();
                }
            }

            System.out.println();

            /* Close Database Connection */

            conn.close();
        }

        catch(Exception e) {
            System.err.println(e.toString());
        }

        /* Close Other Database Objects */

        finally {

            if(resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }

            if(pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }

            if(pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
        }

        return arrayJ;
    }
} 