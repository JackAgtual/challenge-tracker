import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { components } from "@/types/api";
import GoalDefinitionsTable from "./GoalDefinitionsTable";
import CreateGoalForm from "./CreateGoalForm";

export type GoalDefinitionsProps = {
  challengeId: number;
  goalDefinitions: components["schemas"]["GoalDefinitionResponse"][];
};

export default function GoalDefinitionsCard({
  challengeId,
  goalDefinitions,
}: GoalDefinitionsProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Your goals</CardTitle>
      </CardHeader>
      <CardContent>
        {goalDefinitions.length === 0 ? (
          <p>No goal definitions</p>
        ) : (
          <GoalDefinitionsTable
            challengeId={challengeId}
            goalDefinitions={goalDefinitions}
          />
        )}
      </CardContent>
      <CardFooter>
        <CreateGoalForm challengeId={challengeId} />
      </CardFooter>
    </Card>
  );
}
