using PagingMissionControl.Extensions;
using PagingMissionControl.Interfaces;
using System.Collections.Generic;
using System.Linq;

namespace PagingMissionControl.Transforms
{
    public static class TransformInputDataSet
    {
        public static IEnumerable<IOutputRow> ToOutputRows(
            IEnumerable<IInputRow> inputRows)
            => inputRows.Select(r => r.ToOutputRow());
    }
}