package dao;

import org.springframework.stereotype.Repository;
import po.BookType;
import po.Manager;

import java.util.List;

@Repository
public interface ManagerDao {
//    是否存在该管理员账号
    public Integer SelectAdmin(Manager manager);

//用户总数
    public Integer userNum();

    //    书籍总数
    public Integer bookNum();

    public Integer commentNum();

    public Integer huitieNum();

    public Integer userNvNum();

    public Integer userNanNum();

    public List<BookType> selectBookType();

    public void addBookType(String t_type);

    public Integer sameType(BookType bookType);
}
