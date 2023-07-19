package PROJECT_PRM.au.Calendar;

import java.util.ArrayList;
import java.util.List;

class Pagination {
    private final int itemsPerpage, lastPageItem, lastPage;
    private final List<Event> listAllSchedule;

    public Pagination(int itemsPerpage, ArrayList<Event> listAllSchedule){
        this.itemsPerpage = itemsPerpage;
        this.listAllSchedule = listAllSchedule;
        int totalItems = listAllSchedule.size();
        this.lastPage = totalItems / itemsPerpage;
        this.lastPageItem = totalItems % itemsPerpage;
    }

    public ArrayList<Event> generateData(int currentPage){
        int startItem = currentPage * itemsPerpage;
        ArrayList<Event> newPageData = new ArrayList<>();
        if(currentPage == lastPage){
            for(int count = 0; count < lastPageItem; count++){
                if(listAllSchedule.size()<=count)break;
                newPageData.add(listAllSchedule.get(startItem + count));
            }
        }
        else{
            for(int count = 0; count < itemsPerpage; count++){
                if(listAllSchedule.size()<=count)break;
                newPageData.add(listAllSchedule.get(startItem + count));
            }
        }
        return newPageData;
    }

    public int getLastPage(){
        return lastPage;
    }

}
