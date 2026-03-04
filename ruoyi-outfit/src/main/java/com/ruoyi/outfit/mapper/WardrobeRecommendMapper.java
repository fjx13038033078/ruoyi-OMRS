package com.ruoyi.outfit.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 衣物推荐Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeRecommendMapper {

    /**
     * 查询所有公开衣物的特征信息
     *
     * @return 衣物特征列表
     */
    List<Map<String, Object>> selectAllPublicClothesFeatures();

    /**
     * 根据ID列表查询衣物详情
     *
     * @param ids 衣物ID列表
     * @return 衣物详情列表
     */
    List<Map<String, Object>> selectClothesByIds(@Param("ids") List<Long> ids);

    /**
     * 查询用户的收藏行为数据（用于协同过滤）
     * 返回所有用户的收藏记录
     *
     * @return 用户收藏数据
     */
    List<Map<String, Object>> selectAllUserFavorites();

    /**
     * 查询用户自己发布的衣物ID列表
     *
     * @param userId 用户ID
     * @return 衣物ID列表
     */
    List<Long> selectUserOwnedClothesIds(@Param("userId") Long userId);

    /**
     * 查询用户收藏的衣物ID列表
     *
     * @param userId 用户ID
     * @return 衣物ID列表
     */
    List<Long> selectUserFavoriteClothesIds(@Param("userId") Long userId);

    /**
     * 查询用户的风格偏好
     *
     * @param userId 用户ID
     * @return 风格偏好列表
     */
    List<Map<String, Object>> selectUserStylePreferences(@Param("userId") Long userId);

    /**
     * 查询用户的场景偏好
     *
     * @param userId 用户ID
     * @return 场景偏好列表
     */
    List<Map<String, Object>> selectUserScenePreferences(@Param("userId") Long userId);

    /**
     * 根据季节查询衣物
     *
     * @param seasons 季节列表
     * @param excludeIds 需要排除的衣物ID
     * @param limit 限制数量
     * @return 衣物ID列表
     */
    List<Long> selectClothesBySeason(@Param("seasons") List<String> seasons, 
                                      @Param("excludeIds") List<Long> excludeIds,
                                      @Param("limit") int limit);

    /**
     * 根据风格查询衣物
     *
     * @param styles 风格列表
     * @param excludeIds 需要排除的衣物ID
     * @param limit 限制数量
     * @return 衣物ID列表
     */
    List<Long> selectClothesByStyle(@Param("styles") List<String> styles,
                                     @Param("excludeIds") List<Long> excludeIds,
                                     @Param("limit") int limit);

    /**
     * 根据场景查询衣物
     *
     * @param occasions 场景列表
     * @param excludeIds 需要排除的衣物ID
     * @param limit 限制数量
     * @return 衣物ID列表
     */
    List<Long> selectClothesByOccasion(@Param("occasions") List<String> occasions,
                                        @Param("excludeIds") List<Long> excludeIds,
                                        @Param("limit") int limit);

    /**
     * 根据天气条件查询衣物
     *
     * @param seasons 适合的季节
     * @param categories 适合的类别
     * @param materials 适合的材质
     * @param excludeIds 需要排除的衣物ID
     * @param limit 限制数量
     * @return 衣物ID列表
     */
    List<Long> selectClothesByWeather(@Param("seasons") List<String> seasons,
                                       @Param("categories") List<String> categories,
                                       @Param("materials") List<String> materials,
                                       @Param("excludeIds") List<Long> excludeIds,
                                       @Param("limit") int limit);

    /**
     * 查询热门衣物
     *
     * @param excludeIds 需要排除的衣物ID
     * @param limit 限制数量
     * @return 衣物ID列表
     */
    List<Long> selectPopularClothes(@Param("excludeIds") List<Long> excludeIds,
                                     @Param("limit") int limit);

    /**
     * 查询用户位置信息
     *
     * @param userId 用户ID
     * @return 用户位置
     */
    String selectUserLocation(@Param("userId") Long userId);
}
