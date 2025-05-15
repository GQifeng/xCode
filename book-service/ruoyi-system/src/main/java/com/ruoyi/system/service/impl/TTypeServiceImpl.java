package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.TType;
import com.ruoyi.system.service.ITProductService;
import com.ruoyi.system.vo.TProductVo;
import com.ruoyi.system.vo.TTypeVo;
import com.ruoyi.system.mapper.TTypeMapper;
import com.ruoyi.system.service.ITTypeService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类型管理Service业务层处理
 *
 * @author F
 * @date 2023-02-03
 */
@Service
public class TTypeServiceImpl extends ServiceImpl<TTypeMapper, TType> implements ITTypeService {

    @Autowired
    private ITProductService productService;

    @Override
    public TableDataInfo<TTypeVo> queryList(TTypeVo entity) {
        return PageUtils.buildDataInfo(this.baseMapper.queryList(PageUtils.buildPage(), entity));
    }

    @Override
    public List<TTypeVo> queryAll(TTypeVo entity) {
        return this.baseMapper.queryList(entity);
    }

    @Override
    public TTypeVo queryById(Long id) {
        return this.baseMapper.queryById(id);
    }


    // 推荐商品--基于协同过滤算法
    @Override
    public Object getTjList(TProductVo entity) throws Exception {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setDatabaseName("db_shop");
        dataSource.setAllowPublicKeyRetrieval(true);
        dataSource.setUseSSL(false);
        JDBCDataModel dataSqlModel = new MySQLJDBCDataModel(dataSource, "t_like", "user_id", "product_id", "num", "create_time");
        ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(dataSqlModel);
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new GenericItemBasedRecommender(model, itemSimilarity);
            }
        };
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = evaluator.evaluate(recommenderBuilder, null, dataSqlModel, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);

        try {
            List<RecommendedItem> list = recommenderBuilder.buildRecommender(dataSqlModel).recommend(SecurityUtils.getUserId(), 5);
            if (list.size() > 0) {
                for (RecommendedItem recommendation : list) {
                    System.out.printf("(%s,%f)", recommendation.getItemID(), recommendation.getValue());
                }
            }
            List<Long> collect = list.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
            TableDataInfo<TProductVo> dataInfo = productService.queryList(entity.setIds(Joiner.on(",").join(collect)));
            return dataInfo;
        } catch (Exception e) {
            TableDataInfo<TProductVo> dataInfo = productService.queryList(new TProductVo());
            return dataInfo;
        }

    }
}
