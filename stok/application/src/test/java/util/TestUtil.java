package util;

import com.load.impl.DataLoad;
import com.load.options.SortOptions;
import com.payment.stock.common.utils.BeanUtil;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class TestUtil {

    public static DataLoad getDataLoad() {
        String[] filter = {"id", "<>", null};
        List<Object> listFilter = List.of(Arrays.stream(filter).toList());
        DataLoad load = new DataLoad();
        load.setSkip(0);
        load.setTake(10);
        load.setSort(new SortOptions[]{});
        load.setSearchOperation("contains");
        load.setFilter(listFilter);
        load.setCountQuery(true);
        load.setSummaryQuery(true);
        load.setRequireTotalCount(true);
        load.setRequireGroupCount(true);
        load.setDefaultSort("creDate");
        return load;
    }

    public  <T> List<T> getResponse(BeanUtil beanUtil, Object data, Class<T> clazz) {
        return beanUtil.mapAll(List.of(data), clazz, clazz);
    }
}
