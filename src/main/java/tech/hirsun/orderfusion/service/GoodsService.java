package tech.hirsun.orderfusion.service;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.pojo.Goods;
import tech.hirsun.orderfusion.pojo.PageBean;

@Service
public interface GoodsService {

    public Goods getGoodsInfo(Integer id);

    public void update(Goods goods);

    public PageBean page(Integer pagenum, Integer pagesize, String keyword);

    public void add(Goods goods);

    public int minusStock(Integer id, Integer amount);
}
