package dao.JZX;

import org.springframework.stereotype.Repository;
import po.JZX.JBook;
import po.JZX.JShortComm;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface JBookDao {
    public JBook queryBook(Integer id);
    public List<Integer> getTypeID(Integer book_id);
    public String getTypeByTypeID(Integer t_id);

    public void addShortComm(JShortComm sc);
    public List<JShortComm> queryShortComm(Integer shuji_id);
    public String getNameByID(Integer u_id);

    public void dianzan(Integer id);
    public void quxiao(Integer id);
}
