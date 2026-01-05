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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.outfit.domain.WardrobeClothes;
import com.ruoyi.outfit.service.IWardrobeClothesService;
import lombok.RequiredArgsConstructor;

/**
 * 衣物管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/outfit/clothes")
@RequiredArgsConstructor
public class WardrobeClothesController extends BaseController {

    private final IWardrobeClothesService wardrobeClothesService;

    /**
     * 查询衣物列表
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:list')")
    @GetMapping("/list")
    public TableDataInfo list(WardrobeClothes wardrobeClothes) {
        // 普通用户只能查看自己的衣物
        wardrobeClothes.setUserId(getUserId());
        startPage();
        List<WardrobeClothes> list = wardrobeClothesService.selectWardrobeClothesList(wardrobeClothes);
        return getDataTable(list);
    }

    /**
     * 导出衣物列表
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
     * 获取衣物详细信息
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(id);
        // 校验权限：只能查看自己的衣物
        if (clothes != null && !clothes.getUserId().equals(getUserId())) {
            return error("无权查看该衣物信息");
        }
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
        return toAjax(wardrobeClothesService.insertWardrobeClothes(wardrobeClothes));
    }

    /**
     * 修改衣物
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:edit')")
    @Log(title = "衣物管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WardrobeClothes wardrobeClothes) {
        // 校验权限：只能修改自己的衣物
        WardrobeClothes existClothes = wardrobeClothesService.selectWardrobeClothesById(wardrobeClothes.getId());
        if (existClothes == null || !existClothes.getUserId().equals(getUserId())) {
            return error("无权修改该衣物信息");
        }
        wardrobeClothes.setUpdateBy(getUsername());
        return toAjax(wardrobeClothesService.updateWardrobeClothes(wardrobeClothes));
    }

    /**
     * 删除衣物
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:remove')")
    @Log(title = "衣物管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        // 校验权限：只能删除自己的衣物
        for (Long id : ids) {
            WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(id);
            if (clothes == null || !clothes.getUserId().equals(getUserId())) {
                return error("无权删除该衣物信息");
            }
        }
        return toAjax(wardrobeClothesService.deleteWardrobeClothesByIds(ids));
    }

    /**
     * 更新穿着记录
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
     * 切换收藏状态
     */
    @PreAuthorize("@ss.hasPermi('outfit:clothes:edit')")
    @Log(title = "衣物管理", businessType = BusinessType.UPDATE)
    @PutMapping("/favorite/{id}/{isFavorite}")
    public AjaxResult toggleFavorite(@PathVariable Long id, @PathVariable String isFavorite) {
        WardrobeClothes clothes = wardrobeClothesService.selectWardrobeClothesById(id);
        if (clothes == null || !clothes.getUserId().equals(getUserId())) {
            return error("无权操作该衣物");
        }
        return toAjax(wardrobeClothesService.toggleFavorite(id, isFavorite));
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

