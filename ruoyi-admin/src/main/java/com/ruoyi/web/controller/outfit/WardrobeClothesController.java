package com.ruoyi.web.controller.outfit;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.outfit.domain.WardrobeClothes;
import com.ruoyi.outfit.service.IWardrobeClothesService;
import lombok.RequiredArgsConstructor;

/**
 * 衣物管理Controller
 *
 * @author fanjiaxing
 */
@RestController
@RequestMapping("/outfit/clothes")
@RequiredArgsConstructor
public class WardrobeClothesController extends BaseController {

    private final IWardrobeClothesService wardrobeClothesService;

    /**
     * 查询衣物列表（支持多种查询模式）
     * @param wardrobeClothes 查询条件
     * @param scope 查询范围：all-全部公开衣物, mine-我的衣物, favorite-我的收藏
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:list')")
    @GetMapping("/list")
    public TableDataInfo list(WardrobeClothes wardrobeClothes, @RequestParam(defaultValue = "all") String scope) {
        startPage();
        List<WardrobeClothes> list;
        
        switch (scope) {
            case "mine":
                // 查询我的衣物（包括公开和私密的）
                wardrobeClothes.setUserId(getUserId());
                list = wardrobeClothesService.selectWardrobeClothesList(wardrobeClothes);
                break;
            case "favorite":
                // 查询我收藏的衣物
                list = wardrobeClothesService.selectFavoriteClothesList(getUserId(), wardrobeClothes);
                break;
            case "all":
            default:
                // 查询所有公开的衣物
                list = wardrobeClothesService.selectPublicClothesList(wardrobeClothes);
                break;
        }
        
        // 填充当前用户的收藏状态
        wardrobeClothesService.fillCurrentUserFavorite(list, getUserId());
        
        return getDataTable(list);
    }

    /**
     * 导出衣物列表（只导出自己的衣物）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:export')")
    @Log(title = "衣物管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WardrobeClothes wardrobeClothes) {
        wardrobeClothes.setUserId(getUserId());
        List<WardrobeClothes> list = wardrobeClothesService.selectWardrobeClothesList(wardrobeClothes);
        ExcelUtil<WardrobeClothes> util = new ExcelUtil<WardrobeClothes>(WardrobeClothes.class);
        util.exportExcel(response, list, "衣物数据");
    }

    /**
     * 获取衣物详细信息（公开的衣物所有人可查看，私密的只能查看自己的）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(id);
        if (clothes == null) {
            return error("衣物不存在");
        }
        // 私密衣物只能查看自己的
        if ("0".equals(clothes.getIsPublic()) && !clothes.getUserId().equals(getUserId())) {
            return error("无权查看该衣物信息");
        }
        // 填充当前用户的收藏状态
        clothes.setCurrentUserFavorite(wardrobeClothesService.checkFavorite(getUserId(), id));
        return success(clothes);
    }

    /**
     * 新增衣物
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:add')")
    @Log(title = "衣物管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WardrobeClothes wardrobeClothes) {
        wardrobeClothes.setUserId(getUserId());
        wardrobeClothes.setCreateBy(getUsername());
        // 默认公开
        if (wardrobeClothes.getIsPublic() == null) {
            wardrobeClothes.setIsPublic("1");
        }
        // 默认收藏数量为0
        if (wardrobeClothes.getFavoriteCount() == null) {
            wardrobeClothes.setFavoriteCount(0);
        }
        return toAjax(wardrobeClothesService.insertWardrobeClothes(wardrobeClothes));
    }

    /**
     * 修改衣物（普通用户只能修改自己的衣物，管理员可修改任意衣物）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:edit')")
    @Log(title = "衣物管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WardrobeClothes wardrobeClothes) {
        WardrobeClothes existClothes = wardrobeClothesService.selectWardrobeClothesById(wardrobeClothes.getId());
        if (existClothes == null) {
            return error("衣物不存在");
        }
        // 管理员可修改任意衣物，普通用户只能修改自己的
        if (!SecurityUtils.isAdmin(getUserId()) && !existClothes.getUserId().equals(getUserId())) {
            return error("无权修改该衣物信息");
        }
        wardrobeClothes.setUpdateBy(getUsername());
        return toAjax(wardrobeClothesService.updateWardrobeClothes(wardrobeClothes));
    }

    /**
     * 删除衣物（普通用户只能删除自己的衣物，管理员可删除任意衣物）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:remove')")
    @Log(title = "衣物管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        // 管理员可删除任意衣物
        if (!SecurityUtils.isAdmin(getUserId())) {
            // 普通用户只能删除自己的衣物
            for (Long id : ids) {
                WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(id);
                if (clothes == null || !clothes.getUserId().equals(getUserId())) {
                    return error("无权删除该衣物信息");
                }
            }
        }
        return toAjax(wardrobeClothesService.deleteWardrobeClothesByIds(ids));
    }

    /**
     * 更新穿着记录（只能记录自己的衣物）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:edit')")
    @Log(title = "衣物管理", businessType = BusinessType.UPDATE)
    @PutMapping("/wear/{id}")
    public AjaxResult wear(@PathVariable Long id) {
        WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(id);
        if (clothes == null || !clothes.getUserId().equals(getUserId())) {
            return error("无权操作该衣物");
        }
        return toAjax(wardrobeClothesService.updateWearRecord(id));
    }

    /**
     * 切换发布者自己的收藏标记（只能操作自己的衣物）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:edit')")
    @Log(title = "衣物管理", businessType = BusinessType.UPDATE)
    @PutMapping("/selfFavorite/{id}/{isFavorite}")
    public AjaxResult toggleSelfFavorite(@PathVariable Long id, @PathVariable String isFavorite) {
        WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(id);
        if (clothes == null || !clothes.getUserId().equals(getUserId())) {
            return error("无权操作该衣物");
        }
        return toAjax(wardrobeClothesService.toggleFavorite(id, isFavorite));
    }

    /**
     * 收藏衣物（可收藏任何人公开的衣物）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:list')")
    @Log(title = "衣物收藏", businessType = BusinessType.INSERT)
    @PostMapping("/favorite/{clothesId}")
    public AjaxResult addFavorite(@PathVariable Long clothesId) {
        // 检查衣物是否存在且是公开的
        WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(clothesId);
        if (clothes == null) {
            return error("衣物不存在");
        }
        if ("0".equals(clothes.getIsPublic()) && !clothes.getUserId().equals(getUserId())) {
            return error("该衣物为私密衣物，无法收藏");
        }
        int result = wardrobeClothesService.addFavorite(getUserId(), clothesId);
        if (result > 0) {
            return success("收藏成功");
        } else {
            return success("已经收藏过了");
        }
    }

    /**
     * 取消收藏
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:list')")
    @Log(title = "衣物收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/favorite/{clothesId}")
    public AjaxResult removeFavorite(@PathVariable Long clothesId) {
        int result = wardrobeClothesService.removeFavorite(getUserId(), clothesId);
        if (result > 0) {
            return success("已取消收藏");
        } else {
            return success("尚未收藏该衣物");
        }
    }

    /**
     * 切换收藏状态（收藏/取消收藏）
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:list')")
    @Log(title = "衣物收藏", businessType = BusinessType.UPDATE)
    @PutMapping("/favorite/{clothesId}")
    public AjaxResult toggleFavorite(@PathVariable Long clothesId) {
        // 检查衣物是否存在
        WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(clothesId);
        if (clothes == null) {
            return error("衣物不存在");
        }
        // 检查是否已收藏
        boolean isFavorite = wardrobeClothesService.checkFavorite(getUserId(), clothesId);
        if (isFavorite) {
            wardrobeClothesService.removeFavorite(getUserId(), clothesId);
            return success("已取消收藏");
        } else {
            // 检查衣物是否公开
            if ("0".equals(clothes.getIsPublic()) && !clothes.getUserId().equals(getUserId())) {
                return error("该衣物为私密衣物，无法收藏");
            }
            wardrobeClothesService.addFavorite(getUserId(), clothesId);
            return success("收藏成功");
        }
    }

    /**
     * 获取衣柜统计信息
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:list')")
    @GetMapping("/statistics")
    public AjaxResult getStatistics() {
        Map<String, Object> statistics = wardrobeClothesService.getStatistics(getUserId());
        return success(statistics);
    }
}
