package logic;

import dao.StatusDAO;
import entity.Status;
import play.db.jpa.JPA;

import java.util.ArrayList;

/**
 * Created by hanna.kubarka on 31.03.2015.
 */
public class StatusOrder {

    private static StatusDAO statusDAO = new StatusDAO(JPA.em());

    public static ArrayList<Status> statusList = new ArrayList<Status>();

    public static Status nextStatus(Status currentStatus){

        Status newStatus = statusList.get(statusList.indexOf(currentStatus) + 1);
        return  newStatus;
    }

    public static  void setList(ArrayList<Status> list){

        Integer num = 0;
        for(Status status : list){
            String st = status.getTitle();
            num++;
            switch (st){
                case "Новый": {
                    statusList.add(0,status);

                }
                break;
                case "Принят": {
                    statusList.add(1,status);

                }break;
                case "В обработке": {
                    statusList.add(2,status);

                }break;
                case "Готов к доставке": {
                    statusList.add(3,status);

                } break;
                case "Доставка": {
                    statusList.add(4,status);

                }break;
                case "Не выполним": {
                    statusList.add(5,status);

                }break;
                case "Отменен": {
                    statusList.add(6,status);

                }break;
                case "Закрыт": {
                    statusList.add(7,status);

                }break;
                default:
                    statusList.add(status);

            }
        }

    }
}
