package com.ruoyi.web.controller.outfit;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.outfit.domain.WardrobeOutfit;
import com.ruoyi.outfit.service.IWardrobeOutfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 穿搭管理Controller
 *
 * @author fanjiaxing
 */
@RestController
@RequestMapping("/outfit/outfit")
@RequiredArgsConstructor
public class WardrobeOutfitController extends BaseController {

    private final IWardrobeOutfitService wardrobeOutfitService;

    /**
     * 查询穿搭列表（支持多种查询模式）
     * @param wardrobeOutfit 查询条件
     * @param scope 查询范围：all-全部公开穿搭, mine-我的穿搭, favorite-我的收藏
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:list')")
    @GetMapping("/list")
    public TableDataInfo list(WardrobeOutfit wardrobeOutfit, @RequestParam(defaultValue = "all") String scope) {
        startPage();
        List<WardrobeOutfit> list;

        switch (scope) {
            case "mine":
                // 查询我的穿搭（包括公开和私密的）
                wardrobeOutfit.setUserId(getUserId());
                list = wardrobeOutfitService.selectWardrobeOutfitList(wardrobeOutfit);
                break;
            case "favorite":
                // 查询我收藏的穿搭
                list = wardrobeOutfitService.selectFavoriteOutfitList(getUserId(), wardrobeOutfit);
                break;
            case "all":
            default:
                // 查询所有公开的穿搭
                list = wardrobeOutfitService.selectPublicOutfitList(wardrobeOutfit);
                break;
        }

        // 填充当前用户的收藏状态
        wardrobeOutfitService.fillCurrentUserFavorite(list, getUserId());

        return getDataTable(list);
    }

    /**
     * 导出穿搭列表（只导出自己的穿搭）
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:export')")
    @Log(title = "穿搭管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WardrobeOutfit wardrobeOutfit) {
        wardrobeOutfit.setUserId(getUserId());
        List<WardrobeOutfit> list = wardrobeOutfitService.selectWardrobeOutfitList(wardrobeOutfit);
        ExcelUtil<WardrobeOutfit> util = new ExcelUtil<WardrobeOutfit>(WardrobeOutfit.class);
        util.exportExcel(response, list, "穿搭数据");
    }

    /**
     * 获取穿搭详细信息
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        WardrobeOutfit outfit = wardrobeOutfitService.selectWardrobeOutfitById(id);
        if (outfit == null) {
            return error("穿搭不存在");
        }
        // 私密穿搭只能查看自己的
        if ("0".equals(outfit.getIsPublic()) && !outfit.getUserId().equals(getUserId())) {
            return error("无权查看该穿搭信息");
        }
        // 填充当前用户的收藏状态
        outfit.setCurrentUserFavorite(wardrobeOutfitService.checkFavorite(getUserId(), id));
        return success(outfit);
    }

    /**
     * 新增穿搭
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:add')")
    @Log(title = "穿搭管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WardrobeOutfit wardrobeOutfit) {
        wardrobeOutfit.setUserId(getUserId());
        wardrobeOutfit.setCreateBy(getUsername());
        // 默认公开
        if (wardrobeOutfit.getIsPublic() == null) {
            wardrobeOutfit.setIsPublic("1");
        }
        // 默认收藏数量为0
        if (wardrobeOutfit.getFavoriteCount() == null) {
            wardrobeOutfit.setFavoriteCount(0);
        }
        // 默认穿着次数为0
        if (wardrobeOutfit.getWearCount() == null) {
            wardrobeOutfit.setWearCount(0);
        }
        return toAjax(wardrobeOutfitService.insertWardrobeOutfit(wardrobeOutfit));
    }

    /**
     * 修改穿搭（普通用户只能修改自己的穿搭，管理员可修改任意穿搭）
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:edit')")
    @Log(title = "穿搭管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WardrobeOutfit wardrobeOutfit) {
        WardrobeOutfit existOutfit = wardrobeOutfitService.selectWardrobeOutfitById(wardrobeOutfit.getId());
        if (existOutfit == null) {
            return error("穿搭不存在");
        }
        // 管理员可修改任意穿搭，普通用户只能修改自己的
        if (!SecurityUtils.isAdmin(getUserId()) && !existOutfit.getUserId().equals(getUserId())) {
            return error("无权修改该穿搭信息");
        }
        wardrobeOutfit.setUpdateBy(getUsername());
        return toAjax(wardrobeOutfitService.updateWardrobeOutfit(wardrobeOutfit));
    }

    /**
     * 删除穿搭（普通用户只能删除自己的穿搭，管理员可删除任意穿搭）
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:remove')")
    @Log(title = "穿搭管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        // 管理员可删除任意穿搭
        if (!SecurityUtils.isAdmin(getUserId())) {
            // 普通用户只能删除自己的穿搭
            for (Long id : ids) {
                WardrobeOutfit outfit = wardrobeOutfitService.selectWardrobeOutfitById(id);
                if (outfit == null || !outfit.getUserId().equals(getUserId())) {
                    return error("无权删除该穿搭信息");
                }
            }
        }
        return toAjax(wardrobeOutfitService.deleteWardrobeOutfitByIds(ids));
    }

    /**
     * 更新穿着记录（只能记录自己的穿搭）
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:edit')")
    @Log(title = "穿搭管理", businessType = BusinessType.UPDATE)
    @PutMapping("/wear/{id}")
    public AjaxResult wear(@PathVariable Long id) {
        WardrobeOutfit outfit = wardrobeOutfitService.selectWardrobeOutfitById(id);
        if (outfit == null || !outfit.getUserId().equals(getUserId())) {
            return error("无权操作该穿搭");
        }
        return toAjax(wardrobeOutfitService.updateWearRecord(id));
    }

    /**
     * 切换收藏状态（收藏/取消收藏）
     */
    @PreAuthorize("@ss.hasPermi('outfit:outfit:list')")
    @Log(title = "穿搭收藏", businessType = BusinessType.UPDATE)
    @PutMapping("/favorite/{outfitId}")
    public AjaxResult toggleFavorite(@PathVariable Long outfitId) {
        // 检查穿搭是否存在
        WardrobeOutfit outfit = wardrobeOutfitService.selectWardrobeOutfitById(outfitId);
        if (outfit == null) {
            return error("穿搭不存在");
        }
        // 检查是否已收藏
        boolean isFavorite = wardrobeOutfitService.checkFavorite(getUserId(), outfitId);
        if (isFavorite) {
            wardrobeOutfitService.removeFavorite(getUserId(), outfitId);
            return success("已取消收藏");
        } else {
            // 检查穿搭是否公开
            if ("0".equals(outfit.getIsPublic()) && !outfit.getUserId().equals(getUserId())) {
                return error("该穿搭为私密穿搭，无法收藏");
            }
            wardrobeOutfitService.addFavorite(getUserId(), outfitId);
            return success("收藏成功");
        }
    }
}
