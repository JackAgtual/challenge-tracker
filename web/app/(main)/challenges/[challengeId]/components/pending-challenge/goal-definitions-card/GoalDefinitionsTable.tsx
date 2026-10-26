import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Props } from ".";
import { GoalDefinitionActionMenu } from "./GoalDefinitionActionMenu";

export function GoalDefinitionsTable({ challengeId, goalDefinitions }: Props) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Goal</TableHead>
          <TableHead className="text-right">Action</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {goalDefinitions.map((goal) => (
          <TableRow key={goal.id}>
            <TableCell>{goal.name}</TableCell>
            <TableCell className="flex justify-end">
              <GoalDefinitionActionMenu
                challengeId={challengeId}
                goalDefinitionId={goal.id}
              />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
