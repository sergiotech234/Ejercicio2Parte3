import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "RIBERA";
        String password = "ribera";
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("         Menu Principal");
            System.out.println("==============================");
            System.out.println("1. Top 5 ciclistas con mejor rendimiento");
            System.out.println("2. Comparativa de equipos");
            System.out.println("3. Etapas 'especiales'");
            System.out.println("0. Salir");
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                // ================= Top 5 ciclistas con mejor rendimiento =================
                case 1:
                    int opcionCiclistas;
                    do {
                        System.out.println("\n=====================================");
                        System.out.println("Top 5 ciclistas con mejor rendimiento");
                        System.out.println("=====================================");
                        System.out.println("1. Mostrar la comparativa de rendimiento");
                        System.out.println("0. Volver");
                        System.out.print("Opción: ");

                        opcionCiclistas = teclado.nextInt();
                        teclado.nextLine();
// Top 5 ciclistas con mejor rendimeiento
                        switch (opcionCiclistas) {
                            case 1:
                                try (Connection conn = DriverManager.getConnection(url, user, password);
                                     Statement st = conn.createStatement()) {
                                    ResultSet rs = st.executeQuery("select c.nombre as ciclista, e.nombre as equipo, c.nacionalidad as nacionalidad, \n" +
                                            "sum(p.puntos)as total,Round(avg(p.puntos))as promedio, \n" +
                                            "count(distinct p.numero_etapa) as \"numero de etapas\"\n" +
                                            "from ciclista c join participacion p on c.id_ciclista=p.id_ciclista \n" +
                                            "join equipo e on c.id_equipo=e.id_equipo\n" +
                                            "group by c.nombre, e.nombre, c.nacionalidad\n" +
                                            "order by total desc, promedio desc\n" +
                                            "fetch first 5 row only\n");
                                    while (rs.next()) {
                                        System.out.println(
                                                "Ciclista: " + rs.getString("ciclista") +
                                                        " | Equipo: " + rs.getString("equipo") +
                                                        " | País: " + rs.getString("nacionalidad") +
                                                        " | Total: " + rs.getInt("total") +
                                                        " | Promedio: " + rs.getDouble("promedio") +
                                                        " | Etapas: " + rs.getInt("numero de etapas")
                                        );
                                    }
                                } catch (SQLException e) {
                                    System.out.println("ERROR: " + e.getMessage());
                                }
                                break;
                            case 0:
                                System.out.println("Volviendo al menú principal...");
                            break;
                            default:
                                System.out.println("Opción no válida");
                        }

                    } while (opcionCiclistas != 0);

                    break;

                case 2:
                    int opcionEquipos;
                    do {
                        System.out.println("\n==============================");
                        System.out.println("   Comparativa de equipos");
                        System.out.println("==============================");
                        System.out.println("1. Mostrar la comparativa de equipos");
                        System.out.println("0. Volver");
                        System.out.print("Opción: ");

                        opcionEquipos = teclado.nextInt();
                        teclado.nextLine();
// Comparativa equipo
                        switch (opcionEquipos) {
                            case 1:
                                try (Connection conn = DriverManager.getConnection(url, user, password);
                                     Statement st = conn.createStatement()) {
                                    ResultSet rs = st.executeQuery("SELECT E.NOMBRE, E.PAIS, COUNT(DISTINCT C.ID_CICLISTA) AS TOTAL, SUM(P.PUNTOS) AS SUMA, ROUND(AVG(C.EDAD)) AS MEDIA,\n" +
                                            "    (SELECT C2.NOMBRE\n" +
                                            "    FROM CICLISTA C2 JOIN PARTICIPACION P2 ON C2.ID_CICLISTA=P2.ID_CICLISTA\n" +
                                            "    WHERE C2.ID_EQUIPO=E.ID_EQUIPO\n" +
                                            "    GROUP BY C2.NOMBRE\n" +
                                            "    ORDER BY SUM(P2.PUNTOS) DESC\n" +
                                            "    FETCH FIRST 1 ROW ONLY) AS MEJOR\n" +
                                            "FROM EQUIPO E JOIN CICLISTA C ON E.ID_EQUIPO=C.ID_EQUIPO\n" +
                                            "JOIN PARTICIPACION P ON C.ID_CICLISTA=P.ID_CICLISTA\n" +
                                            "GROUP BY E.ID_EQUIPO, E.NOMBRE, E.PAIS\n" +
                                            "ORDER BY SUMA DESC");
                                    while (rs.next()) {
                                        System.out.println(
                                                "Equipo: " + rs.getString("NOMBRE") +
                                                        " | País: " + rs.getString("PAIS") +
                                                        " | Total ciclistas: " + rs.getInt("TOTAL") +
                                                        " | Suma puntos: " + rs.getInt("SUMA") +
                                                        " | Edad media: " + rs.getDouble("MEDIA") +
                                                        " | Mejor ciclista: " + rs.getString("MEJOR")
                                        );
                                    }
                                } catch (SQLException e) {
                                    System.out.println("ERROR: " + e.getMessage());
                                }
                            break;
                            case 0:
                                System.out.println("Volviendo al menú principal...");
                            break;
                            default:
                                System.out.println("Opción no válida");
                        }

                    } while (opcionEquipos != 0);

                    break;
//etapas "especiales"
                case 3:
                    int opcionEtapas;
                    do {
                        System.out.println("\n==============================");
                        System.out.println("     Etapas 'Especiales'");
                        System.out.println("==============================");
                        System.out.println("1. Mostrar las etapas especiales");
                        System.out.println("0. Volver");
                        System.out.print("Opción: ");

                        opcionEtapas = teclado.nextInt();
                        teclado.nextLine();

                        switch (opcionEtapas) {
                            case 1:
                            try (Connection conn = DriverManager.getConnection(url, user, password);
                                 Statement st = conn.createStatement()) {
                                ResultSet rs = st.executeQuery("SELECT DISTINCT E.NUMERO as numero, E.ORIGEN as origen, E.DESTINO as destino, E.FECHA as fecha, E.DISTANCIA_KM as distnacia_km\n" +
                                        "FROM ETAPA E JOIN PARTICIPACION P ON E.NUMERO=P.NUMERO_ETAPA\n" +
                                        "WHERE DISTANCIA_KM >(\n" +
                                        "    SELECT AVG(DISTANCIA_KM)\n" +
                                        "    FROM ETAPA) \n" +
                                        "OR DISTANCIA_KM=(\n" +
                                        "    SELECT MAX(DISTANCIA_KM)\n" +
                                        "    FROM ETAPA)\n" +
                                        "OR DISTANCIA_KM=(\n" +
                                        "    SELECT MIN(DISTANCIA_KM)\n" +
                                        "    FROM ETAPA)\n" +
                                        "OR (\n" +
                                        "    SELECT COUNT(DISTINCT(ID_CICLISTA))\n" +
                                        "    FROM PARTICIPACION\n" +
                                        "    WHERE PUNTOS >0)>10\n" +
                                        "FETCH FIRST 3 ROWS ONLY");
                                while (rs.next()) {
                                    while (rs.next()) {
                                        System.out.println(
                                                "Etapa " + rs.getInt("numero") +
                                                        " | " + rs.getString("origen") + " → " + rs.getString("destino") +
                                                        " | Fecha: " + rs.getString("fecha") +
                                                        " | Distancia: " + rs.getDouble("distnacia_km")
                                        );
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("ERROR: " + e.getMessage());
                            }
                            break;
                            case 0:
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opción no válida");
                        }

                    } while (opcionEtapas != 0);

                    break;

                case 0:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
        teclado.close();
    }
}